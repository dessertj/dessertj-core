package de.spricom.dessert.classfile.attribute;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2022 Hans Jörg Heßmann
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
    public enum AttributeContext {CLASS, FIELD, METHOD, CODE, RECORD}

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
            } else if ("Exceptions".equals(name)) {
                attributes[i] = new ExceptionsAttribute(name, is, constantPool);
            } else if ("InnerClasses".equals(name)) {
                attributes[i] = new InnerClassesAttribute(name, is, constantPool);
            } else if ("EnclosingMethod".equals(name)) {
                attributes[i] = new EnclosingMethodAttribute(name, is, constantPool);
            } else if ("Synthetic".equals(name)) {
                attributes[i] = new SyntheticAttribute(name, is, constantPool);
            } else if ("Signature".equals(name)) {
                attributes[i] = new SignatureAttribute(name, is, constantPool);
            } else if ("SourceFile".equals(name)) {
                attributes[i] = new SourceFileAttribute(name, is, constantPool);
            } else if ("Deprecated".equals(name)) {
                attributes[i] = new DeprecatedAttribute(name, is, constantPool);
            } else if ("RuntimeVisibleAnnotations".equals(name)) {
                attributes[i] = new RuntimeVisibleAnnotationsAttribute(name, is, constantPool);
            } else if ("RuntimeInvisibleAnnotations".equals(name)) {
                attributes[i] = new RuntimeInvisibleAnnotationsAttribute(name, is, constantPool);
            } else if ("RuntimeVisibleParameterAnnotations".equals(name)) {
                attributes[i] = new RuntimeVisibleParameterAnnotationsAttribute(name, is, constantPool);
            } else if ("RuntimeInvisibleParameterAnnotations".equals(name)) {
                attributes[i] = new RuntimeInvisibleParameterAnnotationsAttribute(name, is, constantPool);
            } else if ("RuntimeVisibleTypeAnnotations".equals(name)) {
                attributes[i] = new RuntimeVisibleTypeAnnotationsAttribute(name, is, constantPool);
            } else if ("RuntimeInvisibleTypeAnnotations".equals(name)) {
                attributes[i] = new RuntimeInvisibleTypeAnnotationsAttribute(name, is, constantPool);
            } else if ("AnnotationDefault".equals(name)) {
                attributes[i] = new AnnotationDefaultAttribute(name, is, constantPool);
            } else if ("Module".equals(name)) {
                attributes[i] = new ModuleAttribute(name, is, constantPool);
            } else if ("ModulePackages".equals(name)) {
                attributes[i] = new ModulePackagesAttribute(name, is, constantPool);
            } else if ("ModuleMainClass".equals(name)) {
                attributes[i] = new ModuleMainClassAttribute(name, is, constantPool);
            } else if ("NestHost".equals(name)) {
                attributes[i] = new NestHostAttribute(name, is, constantPool);
            } else if ("NestMembers".equals(name)) {
                attributes[i] = new NestMembersAttribute(name, is, constantPool);
            } else if ("Record".equals(name)) {
                attributes[i] = new RecordAttribute(name, is, constantPool);
            } else if ("PermittedSubclasses".equals(name)) {
                attributes[i] = new PermittedSubclassesAttribute(name, is, constantPool);
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

    protected final void skipLength(DataInputStream is) throws IOException {
        is.readInt(); // skip length
    }

    protected final String[] readClassNames(String name, DataInputStream is, ConstantPool constantPool) throws IOException {
        String[] classNames = new String[is.readUnsignedShort()];
        for (int i = 0; i < classNames.length; i++) {
            String className = constantPool.getConstantClassName(is.readUnsignedShort());
            if (className == null) {
                throw new IllegalArgumentException("No classname at index " + i + " of " + name + " attribute");
            }
            classNames[i] = className;
        }
        return classNames;
    }

    public void addDependentClassNames(Set<String> classNames) {
    }

    public AttributeContext getContext() {
        return context;
    }

    public String toString() {
        return getName();
    }
}
