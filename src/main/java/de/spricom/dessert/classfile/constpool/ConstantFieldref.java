package de.spricom.dessert.classfile.constpool;

import java.util.Set;

class ConstantFieldref extends ConstantPoolEntry {
	public static final int TAG = 9;
	private final int classIndex;
	private final int nameAndTypeIndex;

	public ConstantFieldref(int classIndex, int nameAndTypeIndex) {
		this.classIndex = classIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	@Override
	public String dump() {
		return "fieldref: " + getConstantPoolEntry(classIndex).dump() + ", "
				+ getConstantPoolEntry(nameAndTypeIndex).dump();
	}

	public int getClassIndex() {
		return classIndex;
	}

	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}

	@Override
	public void addDependentClassNames(Set<String> classNames) {
		ConstantNameAndType nameAndType = (ConstantNameAndType) getConstantPoolEntry(nameAndTypeIndex);
		ConstantUtf8 descriptor = (ConstantUtf8) getConstantPoolEntry(nameAndType.getDescriptorIndex());
		new FieldType(descriptor.getValue()).addDependentClassNames(classNames);
	}
}
