package de.spricom.dessert.classfile;

class ConstantMethodType extends ConstantPoolEntry {
	public static final int TAG = 16;
	private final int descriptorIndex;

	public ConstantMethodType(int referenceIndex) {
		this.descriptorIndex = referenceIndex;
	}

	@Override
	public String dump(ClassFile cf) {
		return "methodType: " + cf.getConstantPool()[descriptorIndex].dump(cf);
	}

	public int getDescriptorIndex() {
		return descriptorIndex;
	}
}
