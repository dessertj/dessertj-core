package de.spricom.dessert.classfile;

class ConstantFloat extends ConstantPoolEntry {
	public static final int TAG = 4;
	private final float value;

	public ConstantFloat(float value) {
		this.value = value;
	}

	@Override
	public String dump(ClassFile cf) {
		return "float: " + value;
	}

	public float getValue() {
		return value;
	}
}
