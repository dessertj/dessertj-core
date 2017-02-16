package de.spricom.dessert.classfile;

class ConstantFieldref extends ConstantPoolEntry {
	public static final int TAG = 9;
	private final int classIndex;
	private final int nameAndTypeIndex;

	public ConstantFieldref(int classIndex, int nameAndTypeIndex) {
		this.classIndex = classIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	@Override
	public String dump(ClassFile cf) {
		return "fieldref: " + cf.getConstantPool()[classIndex].dump(cf) + ", "
				+ cf.getConstantPool()[nameAndTypeIndex].dump(cf);
	}

	public int getClassIndex() {
		return classIndex;
	}

	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}
}
