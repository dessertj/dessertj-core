package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;

import de.spricom.dessert.classfile.constpool.ConstantClass;
import de.spricom.dessert.classfile.constpool.ConstantPoolEntry;
import de.spricom.dessert.classfile.constpool.ConstantUtf8;

public class CodeAttribute extends AttributeInfo {
    private int maxStack;
    private int maxLocals;
    private byte[] code;
    private ExceptionTableEntry[] exceptionTable;
    private AttributeInfo[] attributes;

    public CodeAttribute(ConstantUtf8 name, DataInputStream is, ConstantPoolEntry[] constantPoolEntries) throws IOException {
		super(name.getValue());
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
			entry.setCatchType(getConstantClassName(constantPoolEntries, is.readUnsignedShort()));
			exceptionTable[i] = entry;
		}
		setAttributes(AttributeInfo.readAttributes(is, constantPoolEntries));
	}

    private String getConstantClassName(ConstantPoolEntry[] constantPool, int index) {
		ConstantClass clazz = (ConstantClass) constantPool[index];
		if (clazz == null) {
			return null;
		}
		ConstantUtf8 utf8 = (ConstantUtf8) constantPool[clazz.getNameIndex()];
		return utf8.getValue().replace('/', '.');
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
}
