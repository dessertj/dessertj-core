package de.spricom.dessert.classfile;

class ConstantClass extends ConstantPoolEntry {
	public static final int TAG = 7;
	private final int nameIndex;

	public ConstantClass(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	@Override
	public String dump(ClassFile cf) {
		return "class: " + cf.getConstantPool()[nameIndex].dump(cf);
	}

	public int getNameIndex() {
		return nameIndex;
	}
}
