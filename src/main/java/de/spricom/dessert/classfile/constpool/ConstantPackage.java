package de.spricom.dessert.classfile.constpool;

import java.util.BitSet;

class ConstantPackage extends ConstantPoolEntry implements ConstantValue<String>  {
	public static final int TAG = 20;
	private final int nameIndex;

	public ConstantPackage(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	@Override
	void recordReferences(BitSet references) {
		references.set(nameIndex);
	}

	@Override
	public String dump() {
		return "name: " + getConstantPoolEntry(nameIndex).dump();
	}

	public int getNameIndex() {
		return nameIndex;
	}

	@Override
	public String getValue() {
		return getConstantPool().getUtf8String(nameIndex);
	}
}
