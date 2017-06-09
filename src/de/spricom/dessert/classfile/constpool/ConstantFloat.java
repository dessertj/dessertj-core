package de.spricom.dessert.classfile.constpool;

class ConstantFloat extends ConstantPoolEntry {
	public static final int TAG = 4;
	private final float value;

	public ConstantFloat(float value) {
		this.value = value;
	}

	@Override
	public String dump() {
		return "float: " + value;
	}

	public float getValue() {
		return value;
	}
}