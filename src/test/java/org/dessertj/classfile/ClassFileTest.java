package org.dessertj.classfile;

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

import org.dessertj.classfile.attribute.AttributeInfo;
import org.dessertj.classfile.constpool.FieldType;
import org.dessertj.classfile.constpool.MethodType;
import org.fest.assertions.Assertions;
import org.fest.assertions.BooleanAssert;
import org.fest.assertions.CollectionAssert;
import org.fest.assertions.IntAssert;
import org.fest.assertions.ObjectArrayAssert;
import org.fest.assertions.ObjectAssert;
import org.fest.assertions.StringAssert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Tests all methods and there expected return value for
 * {@link ClassFile}
 */
public final class ClassFileTest {

    @Rule
    public TestName testName = new TestName();

    @Test
    public void testThisClass() throws IOException {
        ClassFile cf = new ClassFile(this.getClass());

        assertThat(cf.getThisClass()).isEqualTo(getClass().getName());
        assertThat(cf.getSuperClass()).isEqualTo(Object.class.getName());
        assertThat(cf.getInterfaces()).isEmpty();

        assertThat(cf.getMajorVersion()).isGreaterThanOrEqualTo(50);
        assertThat(cf.getMinorVersion()).isEqualTo(0);

        checkAccessFlags(cf);
        checkDependencies(cf);
        checkFields(cf);
        checkMethods(cf);
        checkConstantPool(cf);

        assertThat(cf.getAttributes()).hasSize(2);
        for (AttributeInfo attributeInfo : cf.getAttributes()) {
            assertThat(attributeInfo.getName()).isIn("SourceFile", "InnerClasses");
            assertThat(attributeInfo.getContext()).isEqualTo(AttributeInfo.AttributeContext.CLASS);
        }
    }

    private void checkAccessFlags(ClassFile cf) {
        assertThat(cf.isAbstract()).isFalse();
        assertThat(cf.isAnnotation()).isFalse();
        assertThat(cf.isEnum()).isFalse();
        assertThat(cf.isFinal()).isTrue();
        assertThat(cf.isInterface()).isFalse();
        assertThat(cf.isSuper()).isTrue();
        assertThat(cf.isSynthetic()).isFalse();
        assertThat(cf.isPublic()).isTrue();
        assertThat(cf.getAccessFlags()).isEqualTo(0x31);
    }

    private void checkDependencies(ClassFile cf) {
        assertThat(cf.getDependentClasses()).containsOnly(
                ClassFile.class.getName(),
                Class.class.getName(),
                Object.class.getName(),
                String.class.getName(),
                Assertions.class.getName(),
                BooleanAssert.class.getName(),
                CollectionAssert.class.getName(),
                IntAssert.class.getName(),
                ObjectArrayAssert.class.getName(),
                StringAssert.class.getName(),
                ObjectAssert.class.getName(),
                Test.class.getName(),
                Rule.class.getName(),
                TestName.class.getName(),
                Collection.class.getName(),
                Set.class.getName(),
                IOException.class.getName(),
                FieldInfo.class.getName(),
                FieldType.class.getName(),
                MethodInfo.class.getName(),
                AttributeInfo.class.getName(),
                AttributeInfo.AttributeContext.class.getName(),
                MethodType.class.getName()
        );
    }

    private void checkFields(ClassFile cf) {
        assertThat(cf.getFields()).hasSize(1);
        FieldInfo fieldInfo = cf.getFields()[0];

        assertThat(fieldInfo.getName()).isEqualTo("testName");
        assertThat(fieldInfo.getDescriptor()).isEqualTo("Lorg/junit/rules/TestName;");
        assertThat(fieldInfo.getDeclaration()).isEqualTo("public org.junit.rules.TestName testName;");
        assertThat(fieldInfo.toString()).isEqualTo(fieldInfo.getDeclaration());
        assertThat(fieldInfo.getAccessFlags()).isEqualTo(0x01);

        FieldType fieldType = fieldInfo.getFieldType();
        assertThat(fieldType.getDeclaration()).isEqualTo(TestName.class.getName());
        assertThat(fieldType.toString()).isEqualTo(fieldType.getDeclaration());
        assertThat(fieldType.getDescriptorLength()).isEqualTo("Lorg/junit/rules/TestName;".length());
        assertThat(fieldType.getObjectTypeClassname()).isEqualTo(TestName.class.getName());
        assertThat(fieldType.getArrayDimensions()).isEqualTo(0);
        assertThat(fieldType.getPrimitiveType()).isNull();

        assertThat(fieldType.isObjectType()).isTrue();
        assertThat(fieldType.isPrimitiveType()).isFalse();
        assertThat(fieldType.isVoidType()).isFalse();
        assertThat(fieldType.isArrayType()).isFalse();

        assertThat(fieldInfo.getAttributes()).hasSize(1);
        AttributeInfo attributeInfo = fieldInfo.getAttributes()[0];
        assertThat(attributeInfo.getName()).isEqualTo("RuntimeVisibleAnnotations");
        assertThat(attributeInfo.getContext()).isEqualTo(AttributeInfo.AttributeContext.FIELD);
    }

    private void checkMethods(ClassFile cf) {
        assertThat(cf.getMethods()).hasSize(8);

        for (MethodInfo methodInfo : cf.getMethods()) {
            if ("testThisClass".equals(methodInfo.getName())) {
                checkTestMethod(methodInfo);
            }
        }

    }

    private void checkTestMethod(MethodInfo methodInfo) {
        assertThat(methodInfo.getDeclaration()).isEqualTo("public void testThisClass();");
        assertThat(methodInfo.toString()).isEqualTo(methodInfo.getDeclaration());
        assertThat(methodInfo.getDescriptor()).isEqualTo("()V");
        assertThat(methodInfo.getAccessFlags()).isEqualTo(0x01);

        MethodType methodType = methodInfo.getMethodType();
        assertThat(methodType.getParameterTypes()).isEmpty();
        assertThat(methodType.getReturnType().isVoidType()).isTrue();

        assertThat(methodInfo.getAttributes()).hasSize(3);
        for (AttributeInfo attributeInfo : methodInfo.getAttributes()) {
            assertThat(attributeInfo.getName()).isIn("RuntimeVisibleAnnotations", "Code", "Exceptions");
            assertThat(attributeInfo.getContext()).isEqualTo(AttributeInfo.AttributeContext.METHOD);
        }
    }

    private void checkConstantPool(ClassFile cf) {
        assertThat(cf.dumpConstantPool()).contains("some string value");
    }
}
