package org.dessertj.partitioning;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2025 Hans Jörg Heßmann
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.dessertj.classfile.ClassFile;
import org.dessertj.classfile.FieldInfo;
import org.dessertj.classfile.MethodInfo;
import org.dessertj.classfile.attribute.*;
import org.dessertj.util.AnnotationPattern;
import org.dessertj.util.Predicate;

import java.util.BitSet;
import java.util.Map;

class AnnotationMatcher implements Predicate<ClassFile> {

    private final AnnotationPattern pattern;

    AnnotationMatcher(AnnotationPattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean test(ClassFile classFile) {
        if (matchesAny(classFile.getAttributes())) {
            return true;
        }
        for (FieldInfo field : classFile.getFields()) {
            if (matchesAny(field.getAttributes())) {
                return true;
            }
        }
        for (MethodInfo method : classFile.getMethods()) {
            if (matchesAny(method.getAttributes())) {
                return true;
            }
            // Code attributes may have RuntimeVisibleTypeAnnotations or RuntimeInvisibleTypeAnnotations.
            for (CodeAttribute code : Attributes.filter(classFile.getAttributes(), CodeAttribute.class)) {
                if (matchesAny(code.getAttributes())) {
                    return true;
                }
            }
        }
        for (RecordAttribute record : Attributes.filter(classFile.getAttributes(), RecordAttribute.class)) {
            for (RecordAttribute.RecordComponentInfo component : record.getComponents()) {
                if (matchesAny(component.getAttributes())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesAny(AttributeInfo[] attributes) {
        for (AttributeInfo attribute : attributes) {
            if (attribute instanceof AbstractAnnotationsAttribute) {
                AbstractAnnotationsAttribute annotationsAttribute =
                        (AbstractAnnotationsAttribute) attribute;
                if (matchesAny(annotationsAttribute.getAnnotations())) {
                    return true;
                }
            } else if (attribute instanceof AbstractParameterAnnotationsAttribute) {
                AbstractParameterAnnotationsAttribute annotationsAttribute =
                        (AbstractParameterAnnotationsAttribute) attribute;
                for (ParameterAnnotation parameterAnnotation : annotationsAttribute.getParameterAnnotations()) {
                    if (matchesAny(parameterAnnotation.getAnnotations())) {
                        return true;
                    }
                }
            } else if (attribute instanceof AbstractTypeAnnotationsAttribute) {
                AbstractTypeAnnotationsAttribute annotationsAttribute =
                        (AbstractTypeAnnotationsAttribute) attribute;
                if (matchesAny(annotationsAttribute.getTypeAnnotations())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesAny(TypeAnnotation[] typeAnnotations) {
        for (TypeAnnotation typeAnnotation : typeAnnotations) {
            if (matches(typeAnnotation.getAnnotation())) {
                return true;
            }
        }
        return false;
    }

    private boolean matchesAny(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (matches(annotation)) {
                return true;
            }
        }
        return false;
    }

    private boolean matches(Annotation annotation) {
        if (!annotation.getType().getObjectTypeClassname().equals(pattern.getAnnotationClassName())) {
            return false;
        }
        for (Map.Entry<String, Object> member : pattern.getAnnotationMembers().entrySet()) {
            if (!hasMember(annotation, member.getKey(), member.getValue())) {
                return false;
            }
        }
        return true;
    }

    private boolean hasMember(Annotation annotation, String key, Object value) {
        for (ElementValuePair elementValuePair : annotation.getElementValuePairs()) {
            if (key.equals(elementValuePair.getName())) {
                return matches(elementValuePair.getValue(), value);
            }
        }
        return false;
    }

    private boolean matches(ElementValue value, Object patternValue) {
        switch (value.getTag()) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 's':
                return patternValue.equals(value.getConstantValue().getValue());
            case 'Z':
                return patternValue.equals(Integer.valueOf(1).equals(value.getConstantValue().getValue()));
            case 'e':
                return patternValue.getClass().getName().equals(value.getType().getDeclaration())
                        && patternValue.toString().equals(value.getConstantValue().getValue());
            case 'c':
                return ((Class<?>) patternValue).getName().equals(value.getType().getDeclaration());
            case '@':
                return new AnnotationMatcher((AnnotationPattern) patternValue).matches(value.getAnnotation());
            case '[':
                return matches(value.getValues(), patternValue);
            default:
                throw new IllegalArgumentException("Invalid ElementValue tag: " + value.getTag());
        }
    }

    private boolean matches(ElementValue[] values, Object patternValue) {
        Object[] patternValues = patternValue instanceof Object[]
                ? (Object[]) patternValue
                : new Object[]{patternValue};
        if (values.length != patternValues.length) {
            return false;
        }
        BitSet matches = new BitSet((patternValues).length);
        for (ElementValue value : values) {
            for (int i = matches.nextClearBit(0);
                 i < patternValues.length; i = matches.nextClearBit(i + 1)) {
                if (matches(value, patternValues[i])) {
                    matches.set(i);
                    break;
                }
            }
        }
        return matches.cardinality() == patternValues.length;
    }
}
