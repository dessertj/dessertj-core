package de.spricom.dessert.classfile.constpool;

import java.util.BitSet;

class ConstantPackage extends ConstantPoolEntry {
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
		return dump(index(nameIndex), getName());
	}

	public String getName() {
		ConstantUtf8 name = getConstantPoolEntry(nameIndex);
		return name.getValue();
	}
}
