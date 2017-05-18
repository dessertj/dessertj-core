package de.spricom.dessert.classfile.constpool;

import de.spricom.dessert.classfile.ClassFile;

class ConstantMethodHandle extends ConstantPoolEntry {
	public static final int TAG = 15;
	private final int referenceKind;
	private final int referenceIndex;

	public ConstantMethodHandle(int referenceKind, int referenceIndex) {
		this.referenceKind = referenceKind;
		this.referenceIndex = referenceIndex;
	}

	@Override
	public String dump(ClassFile cf) {
		return "methodHandle " + referenceIndex + ": " + cf.getConstantPool()[referenceIndex].dump(cf);
	}

	public int getReferenceKind() {
		return referenceKind;
	}

	public int getReferenceIndex() {
		return referenceIndex;
	}
}
