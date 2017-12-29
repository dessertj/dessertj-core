package de.spricom.dessert.classfile.constpool;

class ConstantNameAndType extends ConstantPoolEntry {
	public static final int TAG = 12;
	private final int nameIndex;
	private final int descriptorIndex;

	public ConstantNameAndType(int nameIndex, int descriptorIndex) {
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
	}

	@Override
	public String dump() {
		return "name: " + getConstantPoolEntry(nameIndex).dump() + ", descriptor: "
				+ getConstantPoolEntry(descriptorIndex).dump();
	}

	public int getNameIndex() {
		return nameIndex;
	}

	public int getDescriptorIndex() {
		return descriptorIndex;
	}
}
