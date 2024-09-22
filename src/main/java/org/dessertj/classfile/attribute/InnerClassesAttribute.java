package org.dessertj.classfile.attribute;

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

import org.dessertj.classfile.constpool.ConstantPool;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

/**
 * Represents a
 * <a href="https://docs.oracle.com/javase/specs/jvms/se23/html/jvms-4.html#jvms-4.7.6" target="_blank">
 * Java Virtual Machine Specification: 4.7.6. The InnerClasses Attribute</a>.
 */
public class InnerClassesAttribute extends AttributeInfo {

    private final InnerClass[] innerClasses;

    public InnerClassesAttribute(String name, DataInputStream is, ConstantPool constantPool) throws IOException {
        super(name);
        is.readInt(); // skip length
        innerClasses = new InnerClass[is.readUnsignedShort()];
        for (int i = 0; i < innerClasses.length; i++) {
            innerClasses[i] = new InnerClass(is, constantPool);
        }
    }

    public InnerClass[] getInnerClasses() {
        return innerClasses;
    }

    public void addDependentClassNames(Set<String> classNames) {
        for (InnerClass innerClass : innerClasses) {
            innerClass.addDependentClassNames(classNames);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(":\n");
        for (InnerClass innerClass : innerClasses) {
            sb.append("  ");
            sb.append(innerClass);
            sb.append(";\n");
        }
        return sb.toString();
    }

}
