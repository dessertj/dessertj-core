package de.spricom.dessert.classfile.constpool;

import java.util.BitSet;

class ConstantMethodHandle extends ConstantPoolEntry {
	public static final int TAG = 15;
	private final int referenceKind;
	private final int referenceIndex;

	public ConstantMethodHandle(int referenceKind, int referenceIndex) {
		this.referenceKind = referenceKind;
		this.referenceIndex = referenceIndex;
	}

	@Override
	void recordReferences(BitSet references) {
		references.set(referenceIndex);
	}

	@Override
	public String dump() {
		return "methodHandle " + referenceIndex + ": " + getConstantPoolEntry(referenceIndex).dump();
	}

	public int getReferenceKind() {
		return referenceKind;
	}

	public int getReferenceIndex() {
		return referenceIndex;
	}
}
