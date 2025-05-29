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

/**
 * Represents a
 * <a href="https://docs.oracle.com/javase/specs/jvms/se23/html/jvms-4.html#jvms-4.7.3" target="_blank">
 * Java Virtual Machine Specification: 4.7.3. The Code Attribute</a>.
 */
public class CodeAttribute extends AttributeInfo {
    private int maxStack;
    private int maxLocals;
    private byte[] code;
    private ExceptionTableEntry[] exceptionTable;
    private AttributeInfo[] attributes;

    public CodeAttribute(String name, DataInputStream is, ConstantPool constantPool) throws IOException {
		super(name);
		is.readInt(); // skip length
		maxStack = is.readUnsignedShort();
		maxLocals = is.readUnsignedShort();
		code = new byte[is.readInt()];
		is.readFully(code);
		exceptionTable = new ExceptionTableEntry[is.readUnsignedShort()];
		for (int i = 0; i < exceptionTable.length; i++) {
			ExceptionTableEntry entry = new ExceptionTableEntry();
			entry.setStartPc(is.readUnsignedShort());
			entry.setEndPc(is.readUnsignedShort());
			entry.setHandlerPc(is.readUnsignedShort());
			entry.setCatchType(constantPool.getConstantClassName(is.readUnsignedShort()));
			exceptionTable[i] = entry;
		}
		setAttributes(AttributeInfo.readAttributes(is, constantPool, AttributeContext.CODE));
	}

	public int getMaxStack() {
        return maxStack;
    }

    public void setMaxStack(int maxStack) {
        this.maxStack = maxStack;
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public void setMaxLocals(int maxLocals) {
        this.maxLocals = maxLocals;
    }

    public byte[] getCode() {
        return code;
    }

    public void setCode(byte[] code) {
        this.code = code;
    }

    public ExceptionTableEntry[] getExceptionTable() {
        return exceptionTable;
    }

    public void setExceptionTable(ExceptionTableEntry[] exceptionTable) {
        this.exceptionTable = exceptionTable;
    }

    public AttributeInfo[] getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributeInfo[] attributes) {
        this.attributes = attributes;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(256);
        sb.append(super.toString());
        for (AttributeInfo attribute : attributes) {
            sb.append("\n\t\t\t\t").append(attribute);
        }
        return sb.toString();
    }
}
