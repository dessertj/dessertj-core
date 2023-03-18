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
import org.dessertj.classfile.dependency.DependencyHolder;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

public class TypeAnnotation implements DependencyHolder {

    interface TargetInfo {}

    static class TypeParameterTarget implements TargetInfo {
        final int typeParameterIndex;

        TypeParameterTarget(DataInputStream is, ConstantPool constantPool) throws IOException {
            typeParameterIndex = is.readUnsignedByte();
        }
    }

    static class SuperTypeTarget implements TargetInfo {
        static final int EXTENDS_INDEX = 65535;
        final int superTypeIndex;

        SuperTypeTarget(DataInputStream is, ConstantPool constantPool) throws IOException {
            superTypeIndex = is.readUnsignedShort();
        }
    }

    static class TypeParameterBoundTarget implements TargetInfo {
        final int typeParameterIndex;
        final int boundIndex;

        TypeParameterBoundTarget(DataInputStream is, ConstantPool constantPool) throws IOException {
            typeParameterIndex = is.readUnsignedByte();
            boundIndex = is.readUnsignedByte();
        }
    }

    static class EmptyTarget implements TargetInfo {
        EmptyTarget(DataInputStream is, ConstantPool constantPool) {
        }
    }

    static class FormalParameterTarget implements TargetInfo {
        final int formalParameterIndex;

        FormalParameterTarget(DataInputStream is, ConstantPool constantPool) throws IOException {
            formalParameterIndex = is.readUnsignedByte();
        }
    }

    static class ThrowsTarget implements TargetInfo {
        final int throwsTypeIndex;

        ThrowsTarget(DataInputStream is, ConstantPool constantPool) throws IOException {
            throwsTypeIndex = is.readUnsignedShort();
        }
    }

    static class Table {
        final int startPC;
        final int length;
        final int index;

        Table(DataInputStream is, ConstantPool constantPool) throws IOException {
            startPC = is.readUnsignedShort();
            length = is.readUnsignedShort();
            index = is.readUnsignedShort();
        }
    }

    static class LocalvarTarget implements TargetInfo {
        final Table[] table;

        LocalvarTarget(DataInputStream is, ConstantPool constantPool) throws IOException {
            table = new Table[is.readUnsignedShort()];
            for (int i = 0; i < table.length; i++) {
                table[i] = new Table(is, constantPool);
            }
        }
    }

    static class CatchTarget implements TargetInfo {
        final int exceptionTableIndex;

        CatchTarget(DataInputStream is, ConstantPool constantPool) throws IOException {
            exceptionTableIndex = is.readUnsignedShort();
        }
    }

    static class OffsetTarget implements TargetInfo {
        final int offset;

        OffsetTarget(DataInputStream is, ConstantPool constantPool) throws IOException {
            offset = is.readUnsignedShort();
        }
    }

    static class TypeArgumentTarget implements TargetInfo {
        final int offset;
        final int typeArgumentIndex;

        TypeArgumentTarget(DataInputStream is, ConstantPool constantPool) throws IOException {
            offset = is.readUnsignedShort();
            typeArgumentIndex = is.readUnsignedByte();
        }
    }

    static class Path {
        final int typePathKind;
        final int typeArgumentIndex;

        Path(DataInputStream is, ConstantPool constantPool) throws IOException {
            typePathKind = is.readUnsignedByte();
            typeArgumentIndex = is.readUnsignedByte();
        }
    }

    static class TypePath {
        final Path[] path;

        TypePath(DataInputStream is, ConstantPool constantPool) throws IOException {
            path = new Path[is.readUnsignedByte()];
            for (int i = 0; i < path.length; i++) {
                path[i] = new Path(is, constantPool);
            }
        }
    }

    private final byte targetType;
    private final TargetInfo targetInfo;
    private final TypePath targetPath;
    private final Annotation annotation;

    public TypeAnnotation(DataInputStream is, ConstantPool constantPool) throws IOException {
        targetType = is.readByte();
        targetInfo = readTargetInfo(targetType, is, constantPool);
        targetPath = new TypePath(is, constantPool);
        annotation = new Annotation(is, constantPool);
    }

    private static TargetInfo readTargetInfo(int targetType, DataInputStream is, ConstantPool constantPool) throws IOException {
        switch (targetType) {
            case 0x00: // type parameter declaration of generic class or interface
            case 0x01: // type parameter declaration of generic method or constructor
                return new TypeParameterTarget(is, constantPool);
            case 0x10: // type in extends or implements clause of class declaration (including the direct superclass or direct superinterface of an anonymous class declaration), or in extends clause of interface declaration
                return new SuperTypeTarget(is, constantPool);
            case 0x11: // type in bound of type parameter declaration of generic class or interface
            case 0x12: // type in bound of type parameter declaration of generic method or constructor
                return new TypeParameterBoundTarget(is, constantPool);
            case 0x13: // type in field or record component declaration
            case 0x14: // return type of method, or type of newly constructed object
            case 0x15: // receiver type of method or constructor
                return new EmptyTarget(is, constantPool);
            case 0x16: // type in formal parameter declaration of method, constructor, or lambda expression
                return new FormalParameterTarget(is, constantPool);
            case 0x17: // type in throws clause of method or constructor
                return new ThrowsTarget(is, constantPool);
            case 0x40: // type in local variable declaration
            case 0x41: // type in resource variable declaration
                return new LocalvarTarget(is, constantPool);
            case 0x42: // type in exception parameter declaration
                return new CatchTarget(is, constantPool);
            case 0x43: // type in instanceof expression
            case 0x44: // type in new expression
            case 0x45: // type in method reference expression using ::new
            case 0x46: // type in method reference expression using ::Identifier
                return new OffsetTarget(is, constantPool);
            case 0x47: // type in cast expression
            case 0x48: // type argument for generic constructor in new expression or explicit constructor invocation statement
            case 0x49: // type argument for generic method in method invocation expression
            case 0x4A: // type argument for generic constructor in method reference expression using ::new
            case 0x4B: // type argument for generic method in method reference expression using ::Identifier
                return new TypeArgumentTarget(is, constantPool);
        }
        throw new IllegalArgumentException("Unknown targetType: 0x" + Integer.toHexString(targetType).toUpperCase());
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    @Override
    public void addDependentClassNames(Set<String> classNames) {
        annotation.addDependentClassNames(classNames);
    }

    public String toString() {
        return annotation.toString();
    }
}
