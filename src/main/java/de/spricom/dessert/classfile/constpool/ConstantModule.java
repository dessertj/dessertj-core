package de.spricom.dessert.classfile.constpool;

import java.util.BitSet;

class ConstantModule extends ConstantPoolEntry implements ConstantValue<String>  {
	public static final int TAG = 19;
	private final int nameIndex;

	public ConstantModule(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	@Override
	void recordReferences(BitSet references) {
		references.set(nameIndex);
	}

	@Override
	public String dump() {
		ConstantUtf8 name = getConstantPoolEntry(nameIndex);
		return dump(index(nameIndex), name.getValue());
	}

	public int getNameIndex() {
		return nameIndex;
	}

	@Override
	public String getValue() {
		return getConstantPool().getUtf8String(nameIndex);
	}
}
