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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

/**
 * Represents a
 * <a href="https://docs.oracle.com/javase/specs/jvms/se20/html/jvms-4.html#jvms-4.7.9" target="_blank">
 * Java Virtual Machine Specification: 4.7.9. The Signature Attribute</a>.
 */
public class SignatureAttribute extends AttributeInfo {

    private final String signature;

    public SignatureAttribute(String name, DataInputStream is, ConstantPool constantPool) throws IOException {
        super(name);
        skipLength(is);
        signature = constantPool.getUtf8String(is.readUnsignedShort());
    }

    @Override
    public void addDependentClassNames(Set<String> classNames) {
        SignatureParser parser = new SignatureParser(signature, classNames);
        switch (getContext()) {
            case CLASS:
                parser.parseClassSignature();
                break;
            case METHOD:
                parser.parseMethodSignature();
                break;
            case FIELD:
            case RECORD:
                parser.parseFieldSignature();
                break;
            default:
                throw new IllegalArgumentException("Signature attribute not supported for context " + getContext() + "!");
        }
    }

}
