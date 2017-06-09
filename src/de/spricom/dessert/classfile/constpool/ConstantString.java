package de.spricom.dessert.classfile.constpool;

class ConstantString extends ConstantPoolEntry {
	public static final int TAG = 8;
	private final int stringIndex;

	public ConstantString(int stringIndex) {
		this.stringIndex = stringIndex;
	}

	@Override
	public String dump() {
		return "string: " + getConstantPoolEntry(stringIndex).dump();
	}

	public int getStringIndex() {
		return stringIndex;
	}
}
