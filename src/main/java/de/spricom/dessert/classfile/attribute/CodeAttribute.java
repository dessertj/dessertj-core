package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;

import de.spricom.dessert.classfile.constpool.ConstantPool;

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

    @Override
    public AttributeInfo[] getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributeInfo[] attributes) {
        this.attributes = attributes;
    }
}
