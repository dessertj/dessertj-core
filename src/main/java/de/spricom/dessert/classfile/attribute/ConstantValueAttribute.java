package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;

import de.spricom.dessert.classfile.constpool.ConstantPool;
import de.spricom.dessert.classfile.constpool.ConstantValue;

public class ConstantValueAttribute extends AttributeInfo {
    private final ConstantValue<?> constantValue;

    public ConstantValueAttribute(String name, DataInputStream is, ConstantPool constantPool) throws IOException {
		super(name);
		checkAttributeLength(is, 2, name);
		constantValue = constantPool.getConstantPoolEntry(is.readUnsignedShort());
	}

	public Object getValue() {
		return constantValue.getValue();
	}
}
