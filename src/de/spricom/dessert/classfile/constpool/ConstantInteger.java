package de.spricom.dessert.classfile.constpool;

class ConstantInteger extends ConstantPoolEntry {
	public static final int TAG = 3;
	private final int value;

	public ConstantInteger(int value) {
		this.value = value;
	}

	@Override
	public String dump() {
		return "integer: " + value;
	}

	public int getValue() {
		return value;
	}
}
