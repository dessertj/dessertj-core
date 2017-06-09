package de.spricom.dessert.classfile.constpool;

class ConstantDouble extends ConstantPoolEntry {
	public static final int TAG = 6;
	private final double value;

	public ConstantDouble(double value) {
		this.value = value;
	}

	@Override
	public String dump() {
		return "double: " + value;
	}

	public double getValue() {
		return value;
	}
}
