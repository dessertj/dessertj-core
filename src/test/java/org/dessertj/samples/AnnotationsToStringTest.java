package org.dessertj.samples;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2024 Hans Jörg Heßmann
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
import org.dessertj.classfile.MethodInfo;
import org.dessertj.classfile.attribute.Annotation;
import org.dessertj.classfile.attribute.Attributes;
import org.dessertj.classfile.attribute.ParameterAnnotation;
import org.dessertj.classfile.attribute.RuntimeVisibleAnnotationsAttribute;
import org.dessertj.classfile.attribute.RuntimeVisibleParameterAnnotationsAttribute;
import org.dessertj.samples.annotations.AnnotatedObject;
import org.dessertj.samples.annotations.MetaAnnotatedObject;
import org.dessertj.samples.annotations.SampleAnnotation;
import org.dessertj.samples.annotations.SpecialArgSample;
import org.dessertj.samples.annotations.Val3Annotation;
import org.dessertj.util.ArrayUtils;
import org.dessertj.util.Predicate;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Make sure dessertj produces the same toString value for annotations as the JDK.
 */
public class AnnotationsToStringTest {

    @Test
    public void testAnnotatedObjectSampleAnnotation() throws IOException {
        SampleAnnotation annotation = AnnotatedObject.class.getAnnotation(SampleAnnotation.class);
        assertThat(annotation).isNotNull();

        ClassFile classFile = new ClassFile(AnnotatedObject.class);
        List<RuntimeVisibleAnnotationsAttribute> annotationsAttributes =
                Attributes.filter(classFile.getAttributes(), RuntimeVisibleAnnotationsAttribute.class);
        assertThat(annotationsAttributes).hasSize(1);
        RuntimeVisibleAnnotationsAttribute annotationsAttribute = annotationsAttributes.get(0);
        assertThat(annotationsAttribute.getAnnotations()).hasSize(1);
        Annotation attributeAnnotation = annotationsAttribute.getAnnotations()[0];
        assertThat(attributeAnnotation.toString()).isEqualTo(annotation.toString());
    }

    @Test
    public void testMetaAnnotatedObjectVal3Annotation() throws IOException {
        Val3Annotation annotation = MetaAnnotatedObject.class.getAnnotation(Val3Annotation.class);
        assertThat(annotation).isNotNull();

        ClassFile classFile = new ClassFile(MetaAnnotatedObject.class);
        List<RuntimeVisibleAnnotationsAttribute> annotationsAttributes =
                Attributes.filter(classFile.getAttributes(), RuntimeVisibleAnnotationsAttribute.class);
        assertThat(annotationsAttributes).hasSize(1);
        RuntimeVisibleAnnotationsAttribute annotationsAttribute = annotationsAttributes.get(0);
        assertThat(annotationsAttribute.getAnnotations()).hasSize(1);
        Annotation attributeAnnotation = annotationsAttribute.getAnnotations()[0];
        assertThat(attributeAnnotation.toString()).isEqualTo(annotation.toString());
    }

    @Test
    public void testSpecialArgSampleSpecialArg() throws IOException, NoSuchMethodException {
        final Method method = SpecialArgSample.class.getDeclaredMethod("doSomething", Integer.TYPE);
        java.lang.annotation.Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        assertThat(parameterAnnotations).hasSize(1);
        assertThat(parameterAnnotations[0]).hasSize(1);

        ClassFile classFile = new ClassFile(SpecialArgSample.class);
        List<MethodInfo> methodInfos = ArrayUtils.filter(classFile.getMethods(), new Predicate<MethodInfo>() {
            @Override
            public boolean test(MethodInfo methodInfo) {
                return "doSomething".equals(methodInfo.getName());
            }
        });
        assertThat(methodInfos).hasSize(1);
        List<RuntimeVisibleParameterAnnotationsAttribute> annotationsAttributes =
                Attributes.filter(methodInfos.get(0).getAttributes(),
                        RuntimeVisibleParameterAnnotationsAttribute.class);
        assertThat(annotationsAttributes).hasSize(1);
        ParameterAnnotation[] annotations = annotationsAttributes.get(0).getParameterAnnotations();
        assertThat(annotations).hasSize(1);
        assertThat(annotations[0].toString()).isEqualTo(parameterAnnotations[0][0].toString());
    }
}
