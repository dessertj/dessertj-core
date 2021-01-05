package de.spricom.dessert.classfile.constpool;

import java.util.BitSet;

class ConstantString extends ConstantPoolEntry implements ConstantValue<String>  {
	public static final int TAG = 8;
	private final int stringIndex;

	public ConstantString(int stringIndex) {
		this.stringIndex = stringIndex;
	}

	@Override
	void recordReferences(BitSet references) {
		references.set(stringIndex);
	}

	@Override
	public String dump() {
		return "\"" + getConstantPoolEntry(stringIndex).dump() + "\"";
	}

	public int getStringIndex() {
		return stringIndex;
	}

	@Override
	public String getValue() {
		return getConstantPool().getUtf8String(stringIndex);
	}
}
