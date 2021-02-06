package de.spricom.dessert.classfile.attribute;

/*-
 * #%L
 * Dessert Dependency Assertion Library
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
import de.spricom.dessert.classfile.dependency.DependencyHolder;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

public abstract class AttributeInfo implements DependencyHolder {
    public enum AttributeContext {CLASS, FIELD, METHOD, CODE}

    private final String name;
    private AttributeContext context;

    public static AttributeInfo[] readAttributes(DataInputStream is, ConstantPool constantPool, AttributeContext context) throws IOException {
        AttributeInfo[] attributes = new AttributeInfo[is.readUnsignedShort()];
        for (int i = 0; i < attributes.length; i++) {
            String name = constantPool.getUtf8String(is.readUnsignedShort());
            if ("ConstantValue".equals(name)) {
                attributes[i] = new ConstantValueAttribute(name, is, constantPool);
            } else if ("Code".equals(name)) {
                attributes[i] = new CodeAttribute(name, is, constantPool);
            } else if ("RuntimeVisibleAnnotations".equals(name)) {
                attributes[i] = new RuntimeVisibleAnnotationsAttribute(name, is, constantPool);
            } else if ("RuntimeVisibleParameterAnnotations".equals(name)) {
                attributes[i] = new RuntimeVisibleParameterAnnotationsAttribute(name, is, constantPool);
            } else if ("Signature".equals(name)) {
                attributes[i] = new SignatureAttribute(name, is, constantPool);
            } else if ("EnclosingMethod".equals(name)) {
                attributes[i] = new EnclosingMethodAttribute(name, is, constantPool);
            } else if ("InnerClasses".equals(name)) {
                attributes[i] = new InnerClassesAttribute(name, is, constantPool);
            } else {
                attributes[i] = new UnknownAttribute(name, is);
            }
            attributes[i].context = context;
        }
        return attributes;
    }

    public AttributeInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected final void checkAttributeLength(DataInputStream is, int expectedLength, String name) throws IOException {
        int len;
        if ((len = is.readInt()) != expectedLength) {
            throw new IOException("Unexpected length of " + len + " for attribute " + name);
        }
    }

    public void addDependentClassNames(Set<String> classNames) {
    }

    public AttributeContext getContext() {
        return context;
    }

    public AttributeInfo[] getAttributes() {
        return new AttributeInfo[0];
    }

    public String toString() {
        return getName();
    }
}
