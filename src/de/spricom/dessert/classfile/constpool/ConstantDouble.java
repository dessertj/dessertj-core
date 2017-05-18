package de.spricom.dessert.classfile.constpool;

import de.spricom.dessert.classfile.ClassFile;

class ConstantDouble extends ConstantPoolEntry {
	public static final int TAG = 6;
	private final double value;

	public ConstantDouble(double value) {
		this.value = value;
	}

	@Override
	public String dump(ClassFile cf) {
		return "double: " + value;
	}

	public double getValue() {
		return value;
	}
}
