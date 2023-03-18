package org.dessertj.classfile.attribute;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2023 Hans Jörg Heßmann
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
import org.dessertj.classfile.constpool.FieldType;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

/**
 * Represents a
 * <a href="https://docs.oracle.com/javase/specs/jvms/se19/html/jvms-4.html#jvms-4.7.30" target="_blank">
 * Java Virtual Machine Specification: 4.7.30. The Record Attribute</a>.
 */
public class RecordAttribute extends AttributeInfo {

    public static class RecordComponentInfo {
        private final String name;
        private final FieldType type;
        private final AttributeInfo[] attributes;

        private RecordComponentInfo(DataInputStream is, ConstantPool constantPool) throws IOException {
            name = constantPool.getUtf8String(is.readUnsignedShort());
            type = constantPool.getFieldType(is.readUnsignedShort());
            attributes = AttributeInfo.readAttributes(is, constantPool, AttributeContext.RECORD);
        }

        public String getName() {
            return name;
        }

        public FieldType getType() {
            return type;
        }

        public AttributeInfo[] getAttributes() {
            return attributes;
        }

        public void addDependentClassNames(Set<String> classNames) {
            type.addDependentClassNames(classNames);
            for (AttributeInfo attribute : attributes) {
                attribute.addDependentClassNames(classNames);
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            appendString(sb);
            return sb.toString();
        }

        void appendString(StringBuilder sb) {
            sb.append(type.toString());
            sb.append(" ").append(name);
            for (AttributeInfo attribute : attributes) {
                sb.append("\n\t\t\t\t").append(attribute);
            }
        }
    }

    private final RecordComponentInfo[] components;

    public RecordAttribute(String name, DataInputStream is, ConstantPool constantPool) throws IOException {
        super(name);
        skipLength(is);
        components = new RecordComponentInfo[is.readUnsignedShort()];
        for (int i = 0; i < components.length; i++) {
            components[i] = new RecordComponentInfo(is, constantPool);
        }
    }

    public void addDependentClassNames(Set<String> classNames) {
        for (RecordComponentInfo component : components) {
            component.addDependentClassNames(classNames);
        }
    }

    public RecordComponentInfo[] getComponents() {
        return components;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(":\n");
        for (RecordComponentInfo component : components) {
            sb.append("  ");
            component.appendString(sb);
            sb.append(";\n");
        }
        return sb.toString();
    }
}
