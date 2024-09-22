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
import org.dessertj.classfile.dependency.DependencyHolder;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

public class InnerClass implements DependencyHolder {
    public static final int ACC_PUBLIC = 0x0001; // Marked or implicitly public in source.
    public static final int ACC_PRIVATE = 0x0002; // Marked private in source.
    public static final int ACC_PROTECTED = 0x0004; // Marked protected in source.
    public static final int ACC_STATIC = 0x0008; // Marked or implicitly static in source.
    public static final int ACC_FINAL = 0x0010; // Marked or implicitly final in source.
    public static final int ACC_INTERFACE = 0x0200; //  Was an interface in source.
    public static final int ACC_ABSTRACT = 0x0400; // Marked or implicitly abstract in source.
    public static final int ACC_SYNTHETIC = 0x1000; // Declared synthetic; not present in the source code.
    public static final int ACC_ANNOTATION = 0x2000; // Declared as an annotation type.
    public static final int ACC_ENUM = 0x4000; // Declared as an enum type.

    private final String innerClassName;
    private final String outerClassName;
    private final String simpleName;
    private final int accessFlags;

    public InnerClass(DataInputStream is, ConstantPool constantPool) throws IOException {
        innerClassName = constantPool.getConstantClassName(is.readUnsignedShort());
        outerClassName = constantPool.getConstantClassName(is.readUnsignedShort());
        int simpleNameIndex = is.readUnsignedShort();
        simpleName = simpleNameIndex == 0 ? null : constantPool.getUtf8String(simpleNameIndex);
        accessFlags = is.readUnsignedShort();
    }

    @Override
    public void addDependentClassNames(Set<String> classNames) {
    }

    public boolean isIndependentOfOuterClass(String thisClass) {
        return isStatic() && thisClass.equals(innerClassName);
    }

    public boolean isStatic() {
        return (accessFlags & ACC_STATIC) != 0;
    }

    public String getInnerClassName() {
        return innerClassName;
    }

    public String getOuterClassName() {
        return outerClassName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public int getAccessFlags() {
        return accessFlags;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (is(ACC_PUBLIC)) {
            sb.append("public ");
        }
        if (is(ACC_PROTECTED)) {
            sb.append("protected ");
        }
        if (is(ACC_PRIVATE)) {
            sb.append("private ");
        }
        if (is(ACC_STATIC)) {
            sb.append("static ");
        }
        if (is(ACC_FINAL)) {
            sb.append("final ");
        }
        if (is(ACC_INTERFACE)) {
            sb.append("interface ");
        }
        if (is(ACC_ABSTRACT)) {
            sb.append("abstract ");
        }
        if (is(ACC_SYNTHETIC)) {
            sb.append("synthetic ");
        }
        if (is(ACC_ANNOTATION)) {
            sb.append("@interface ");
        }
        if (is(ACC_ENUM)) {
            sb.append("enum ");
        }
        sb.append(innerClassName);
        if (outerClassName != null) {
            sb.append(" within ").append(outerClassName);
        }
        if (simpleName != null) {
            sb.append(" called ").append(simpleName);
        }
        return sb.toString();
    }

    private boolean is(int accessFlag) {
        return (accessFlags & accessFlag) == accessFlag;
    }
}
