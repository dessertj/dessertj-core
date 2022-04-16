package de.spricom.dessert.classfile;

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

import de.spricom.dessert.classfile.attribute.AttributeInfo;
import de.spricom.dessert.classfile.attribute.AttributeInfo.AttributeContext;
import de.spricom.dessert.classfile.constpool.ConstantPool;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.TreeSet;

/**
 * Wraps the information contained in a .class file according
 * to the <a href="https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html" target="_blank">
 * Java Virtual Machine Specification</a>.
 */
public class ClassFile {
    public static final int MAGIC = 0xCAFEBABE;

    public static final int ACC_PUBLIC = 0x0001; // Declared public; may be accessed from outside its package.
    public static final int ACC_FINAL = 0x0010; // Declared final; no subclasses allowed.
    public static final int ACC_SUPER = 0x0020; // Treat superclass methods specially when invoked by the invokespecial instruction.
    public static final int ACC_INTERFACE = 0x0200; // Is an interface, not a class.
    public static final int ACC_ABSTRACT = 0x0400; // Declared abstract; must not be instantiated.
    public static final int ACC_SYNTHETIC = 0x1000; // Declared synthetic; not present in the source code.
    public static final int ACC_ANNOTATION = 0x2000; // Declared as an annotation type.
    public static final int ACC_ENUM = 0x4000; // Declared as an enum type.
    public static final int ACC_MODULE = 0x8000; // Is a module, not a class or interface.

    private final int minorVersion;
    private final int majorVersion;
    private final ConstantPool constantPool;
    private final int accessFlags;
    private final String thisClass;
    private final String superClass;
    private String[] interfaces;
    private FieldInfo[] fields;
    private MethodInfo[] methods;
    private final AttributeInfo[] attributes;

    public ClassFile(Class<?> clazz) throws IOException {
        this(open(clazz));
    }

    private static InputStream open(Class<?> clazz) {
        InputStream is = clazz.getResourceAsStream("/" + clazz.getName().replace('.', '/') + ".class");
        assert is != null : "No file found for " + clazz;
        return is;
    }

    public ClassFile(InputStream in) throws IOException {
        BufferedInputStream bi = new BufferedInputStream(in);
        try {
            DataInputStream is = new DataInputStream(bi);
            if (ClassFile.MAGIC != is.readInt()) {
                throw new IOException("Not a class file.");
            }
            minorVersion = is.readUnsignedShort();
            majorVersion = is.readUnsignedShort();
            constantPool = new ConstantPool(is);
            accessFlags = is.readUnsignedShort();
            thisClass = constantPool.getConstantClassName(is.readUnsignedShort());
            superClass = constantPool.getConstantClassName(is.readUnsignedShort());
            readInterfaces(is);
            readFields(is);
            readMethods(is);
            attributes = AttributeInfo.readAttributes(is, constantPool, AttributeContext.CLASS);
            if (is.read() != -1) {
                throw new IOException("EOF not reached!");
            }
        } finally {
            bi.close();
        }
    }

    private void readInterfaces(DataInputStream is) throws IOException {
        int interfacesCount = is.readUnsignedShort();
        interfaces = new String[interfacesCount];
        for (int i = 0; i < interfacesCount; i++) {
            interfaces[i] = constantPool.getConstantClassName(is.readUnsignedShort());
        }
    }

    private void readFields(DataInputStream is) throws IOException {
        int fieldCount = is.readUnsignedShort();
        fields = new FieldInfo[fieldCount];
        for (int i = 0; i < fieldCount; i++) {
            fields[i] = new FieldInfo();
            readMember(fields[i], is, AttributeContext.FIELD);
        }
    }

    private void readMethods(DataInputStream is) throws IOException {
        int methodCount = is.readUnsignedShort();
        methods = new MethodInfo[methodCount];
        for (int i = 0; i < methodCount; i++) {
            methods[i] = new MethodInfo();
            readMember(methods[i], is, AttributeContext.METHOD);
        }
    }

    private void readMember(MemberInfo member, DataInputStream is, AttributeContext context) throws IOException {
        member.setAccessFlags(is.readUnsignedShort());
        member.setName(readString(is));
        member.setDescriptor(readString(is));
        member.setAttributes(AttributeInfo.readAttributes(is, constantPool, context));
    }

    private String readString(DataInputStream is) throws IOException {
        return constantPool.getUtf8String(is.readUnsignedShort());
    }

    public Set<String> getDependentClasses() {
        Set<String> classNames = new TreeSet<String>();
        for (FieldInfo fieldInfo : fields) {
            fieldInfo.addDependentClassNames(classNames);
        }
        for (MethodInfo methodInfo : methods) {
            methodInfo.addDependentClassNames(classNames);
        }
        for (AttributeInfo attribute : attributes) {
            attribute.addDependentClassNames(classNames);
        }
        constantPool.addDependentClassNames(classNames);
        classNames.remove(thisClass);
        return classNames;
    }

    public String dumpConstantPool() {
        return constantPool.dumpConstantPool();
    }

    /**
     * Produces a dump similar to <i>javap -verbose</i>.
     *
     * @return the dump
     */
    public String dump() {
        StringBuilder sb = new StringBuilder();
        sb.append(constantPool.dumpConstantPool());
        String indent = "\t";


        sb.append("Interfaces:\n");
        for (String ifc : getInterfaces()) {
            sb.append(indent).append(ifc).append("\n");
        }
        sb.append("Fields:\n");
        for (FieldInfo field : getFields()) {
            sb.append(indent).append(field).append("\n");
            dump(field.getAttributes(), sb, indent + indent);
        }
        sb.append("Methods:\n");
        for (MethodInfo method : getMethods()) {
            sb.append(indent).append(method).append("\n");
            dump(method.getAttributes(), sb, indent + indent);
        }
        sb.append("Class attributes:\n");
        dump(getAttributes(), sb, indent);

        sb.append("Dependent classes:\n");
        for (String dependentClass : getDependentClasses()) {
            sb.append("  ").append(dependentClass).append("\n");
        }
        return sb.toString();
    }

    private void dump(AttributeInfo[] attributes, StringBuilder sb, String indent) {
        for (AttributeInfo attribute : attributes) {
            sb.append(indent).append("attribute: ").append(attribute).append("\n");
        }
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getAccessFlags() {
        return accessFlags;
    }

    public String getThisClass() {
        return thisClass;
    }

    public String getSuperClass() {
        return superClass;
    }

    public String[] getInterfaces() {
        return interfaces;
    }

    public FieldInfo[] getFields() {
        return fields;
    }

    public MethodInfo[] getMethods() {
        return methods;
    }

    public AttributeInfo[] getAttributes() {
        return attributes;
    }

    public boolean isPublic() {
        return (accessFlags & ACC_PUBLIC) != 0;
    }

    public boolean isFinal() {
        return (accessFlags & ACC_FINAL) != 0;
    }

    public boolean isSuper() {
        return (accessFlags & ACC_SUPER) != 0;
    }

    public boolean isInterface() {
        return (accessFlags & ACC_INTERFACE) != 0;
    }

    public boolean isAbstract() {
        return (accessFlags & ACC_ABSTRACT) != 0;
    }

    public boolean isSynthetic() {
        return (accessFlags & ACC_SYNTHETIC) != 0;
    }

    public boolean isAnnotation() {
        return (accessFlags & ACC_ANNOTATION) != 0;
    }

    public boolean isEnum() {
        return (accessFlags & ACC_ENUM) != 0;
    }

    public boolean isModule() {
        return (accessFlags & ACC_MODULE) != 0;
    }
}
