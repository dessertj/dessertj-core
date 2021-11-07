package de.spricom.dessert.classfile.attribute;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2021 Hans Jörg Heßmann
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

import de.spricom.dessert.classfile.constpool.ConstantPool;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class ExceptionsAttribute extends AttributeInfo {

    private final String[] exceptions;

    public ExceptionsAttribute(String name, DataInputStream is, ConstantPool constantPool) throws IOException {
        super(name);
        is.readInt(); // skip length
        exceptions = new String[is.readUnsignedShort()];
        for (int i = 0; i < exceptions.length; i++) {
            String exceptionClassName = constantPool.getConstantClassName(is.readUnsignedShort());
            if (exceptionClassName == null) {
                throw new IllegalArgumentException("No classname at index " + i + " of exception attribute");
            }
            exceptions[i] = exceptionClassName;
        }
    }

    public String[] getExceptions() {
        return exceptions;
    }

    public void addDependentClassNames(Set<String> classNames) {
        Collections.addAll(classNames, exceptions);
    }
}
