package de.spricom.dessert.classfile;

class ConstantMethodref extends ConstantPoolEntry {
	public static final int TAG = 10;
	private final int classIndex;
	private final int nameAndTypeIndex;

	public ConstantMethodref(int classIndex, int nameAndTypeIndex) {
		this.classIndex = classIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	@Override
	public String dump(ClassFile cf) {
		return "methodref: " + cf.getConstantPool()[classIndex].dump(cf) + ", "
				+ cf.getConstantPool()[nameAndTypeIndex].dump(cf);
	}

	public int getClassIndex() {
		return classIndex;
	}

	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}
}
