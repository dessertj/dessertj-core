package de.spricom.dessert.classfile;

class ConstantNameAndType extends ConstantPoolEntry {
	public static final int TAG = 12;
	private final int nameIndex;
	private final int descriptorIndex;

	public ConstantNameAndType(int nameIndex, int descriptorIndex) {
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
	}

	@Override
	public String dump(ClassFile cf) {
		return "name: " + cf.getConstantPool()[nameIndex].dump(cf) + ", descriptor: "
				+ cf.getConstantPool()[descriptorIndex].dump(cf);
	}

	public int getNameIndex() {
		return nameIndex;
	}

	public int getDescriptorIndex() {
		return descriptorIndex;
	}
}
