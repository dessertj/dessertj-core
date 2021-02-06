package de.spricom.dessert.classfile.constpool;

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

import de.spricom.dessert.classfile.dependency.DependencyHolder;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.Set;

public final class ConstantPool implements DependencyHolder {
    private final ConstantPoolEntry[] entries;
    private final BitSet referenced;

    public ConstantPool(DataInputStream is) throws IOException {
        entries = new ConstantPoolEntry[is.readUnsignedShort()];
        referenced = new BitSet(entries.length);
        int index = 1;
        while (index < entries.length) {
            int offset = 1;
            int tag = is.readUnsignedByte();
            switch (tag) {
                case ConstantUtf8.TAG:
                    entries[index] = new ConstantUtf8(is.readUTF());
                    break;
                case ConstantInteger.TAG:
                    entries[index] = new ConstantInteger(is.readInt());
                    break;
                case ConstantFloat.TAG:
                    entries[index] = new ConstantFloat(is.readFloat());
                    break;
                case ConstantLong.TAG:
                    entries[index] = new ConstantLong(is.readLong());
                    offset = 2;
                    break;
                case ConstantDouble.TAG:
                    entries[index] = new ConstantDouble(is.readDouble());
                    offset = 2;
                    break;
                case ConstantClass.TAG:
                    entries[index] = new ConstantClass(is.readUnsignedShort());
                    break;
                case ConstantString.TAG:
                    entries[index] = new ConstantString(is.readUnsignedShort());
                    break;
                case ConstantFieldref.TAG:
                    entries[index] = new ConstantFieldref(is.readUnsignedShort(), is.readUnsignedShort());
                    break;
                case ConstantMethodref.TAG:
                    entries[index] = new ConstantMethodref(is.readUnsignedShort(), is.readUnsignedShort());
                    break;
                case ConstantInterfaceMethodref.TAG:
                    entries[index] = new ConstantInterfaceMethodref(is.readUnsignedShort(), is.readUnsignedShort());
                    break;
                case ConstantNameAndType.TAG:
                    entries[index] = new ConstantNameAndType(is.readUnsignedShort(), is.readUnsignedShort());
                    break;
                case ConstantMethodHandle.TAG:
                    entries[index] = new ConstantMethodHandle(is.readUnsignedByte(), is.readUnsignedShort());
                    break;
                case ConstantMethodType.TAG:
                    entries[index] = new ConstantMethodType(is.readUnsignedShort());
                    break;
                case ConstantDynamic.TAG:
                    entries[index] = new ConstantDynamic(is.readUnsignedShort(), is.readUnsignedShort());
                    break;
                case ConstantInvokeDynamic.TAG:
                    entries[index] = new ConstantInvokeDynamic(is.readUnsignedShort(), is.readUnsignedShort());
                    break;
                case ConstantModule.TAG:
                    entries[index] = new ConstantModule(is.readUnsignedShort());
                    break;
                case ConstantPackage.TAG:
                    entries[index] = new ConstantPackage(is.readUnsignedShort());
                    break;
                default:
                    throw new IOException("Unknown constant-pool tag: " + tag);
            }
            entries[index].setConstantPool(this);
            entries[index].recordReferences(referenced);
            index += offset;
        }
    }

    public String dumpConstantPool() {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (ConstantPoolEntry entry : entries) {
            if (entry != null) {
                sb.append(String.format("%6s: %-18s %s%n", ConstantPoolEntry.index(index),
                        entry.typeName() + referenced(index),
                        entry.dump()));
            }
            index++;
        }
        return sb.toString();
    }

    private String referenced(int index) {
        return referenced.get(index) ? "." : "";
    }

    @SuppressWarnings("unchecked")
    public <T> T getConstantPoolEntry(int index) {
        referenced.set(index);
        ConstantPoolEntry entry = entries[index];
        return (T) entry;
    }

    public String getUtf8String(int index) {
        ConstantUtf8 entry = getConstantPoolEntry(index);
        return entry.getValue();
    }

    public String getConstantClassName(int index) {
        ConstantClass clazz = getConstantPoolEntry(index);
        if (clazz == null) {
            return null;
        }
        return clazz.getName();
    }

    public FieldType getFieldType(int index) {
        ConstantUtf8 entry = getConstantPoolEntry(index);
        return new FieldType(entry.getValue());
    }

    public String getNameAndTypeName(int index) {
        ConstantNameAndType nameAndType = getConstantPoolEntry(index);
        return nameAndType.getName();
    }

    public MethodType getNameAndTypeMethodType(int index) {
        ConstantNameAndType nameAndType = getConstantPoolEntry(index);
        return new MethodType(nameAndType.getDescriptor());
    }

    @Override
    public void addDependentClassNames(Set<String> classNames) {
        for (int i = 1; i < entries.length; i++) {
            ConstantPoolEntry entry = entries[i];
            if (entry != null) {
                entry.addDependentClassNames(classNames);
            }
        }
    }
}
