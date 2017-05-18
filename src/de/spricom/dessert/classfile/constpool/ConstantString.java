package de.spricom.dessert.classfile.constpool;

import de.spricom.dessert.classfile.ClassFile;

class ConstantString extends ConstantPoolEntry {
	public static final int TAG = 8;
	private final int stringIndex;

	public ConstantString(int stringIndex) {
		this.stringIndex = stringIndex;
	}

	@Override
	public String dump(ClassFile cf) {
		return "string: " + cf.getConstantPool()[stringIndex].dump(cf);
	}

	public int getStringIndex() {
		return stringIndex;
	}
}
