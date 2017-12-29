package de.spricom.dessert.classfile.constpool;

class ConstantDouble extends ConstantPoolEntry implements ConstantValue<Double> {
	public static final int TAG = 6;
	private final double value;

	public ConstantDouble(double value) {
		this.value = value;
	}

	@Override
	public String dump() {
		return "double: " + value;
	}

	public Double getValue() {
		return value;
	}
}
