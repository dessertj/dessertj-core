package org.dessertj.classfile.attribute;

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

import org.dessertj.classfile.constpool.ConstantPool;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

/**
 * Represents a
 * <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.7.28" target="_blank">
 * Java Virtual Machine Specification: 4.7.28. The NestHost Attribute</a>.
 */
public class NestHostAttribute extends AttributeInfo {

    private final String hostClassName;

    public NestHostAttribute(String name, DataInputStream is, ConstantPool constantPool) throws IOException {
        super(name);
        skipLength(is);
        hostClassName = constantPool.getConstantClassName(is.readUnsignedShort());
    }

    public String getHostClassName() {
        return hostClassName;
    }

    public void addDependentClassNames(Set<String> classNames) {
        classNames.add(hostClassName);
    }

    @Override
    public String toString() {
        return getName() + ": " + hostClassName;
    }
}
