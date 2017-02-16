package de.spricom.dessert.classfile;

class ConstantInterfaceMethodref extends ConstantPoolEntry {
	public static final int TAG = 11;
	private final int classIndex;
	private final int nameAndTypeIndex;

	public ConstantInterfaceMethodref(int classIndex, int nameAndTypeIndex) {
		this.classIndex = classIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	@Override
	public String dump(ClassFile cf) {
		return "interface methodref: " + cf.getConstantPool()[classIndex].dump(cf) + ", "
				+ cf.getConstantPool()[nameAndTypeIndex].dump(cf);
	}

	public int getClassIndex() {
		return classIndex;
	}

	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}
}
