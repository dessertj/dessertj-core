package de.spricom.dessert.classfile.constpool;

import java.util.Set;

class ConstantNameAndType extends ConstantPoolEntry {
	public static final int TAG = 12;
	private final int nameIndex;
	private final int descriptorIndex;

	public ConstantNameAndType(int nameIndex, int descriptorIndex) {
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
	}

	@Override
	String dump() {
		return dump(index(nameIndex) + ":" + index(descriptorIndex),
				getName() + ": " + getDescriptor());
	}

	public String getName() {
		ConstantUtf8 name = getConstantPoolEntry(nameIndex);
		return name.getValue();
	}

	public String getDescriptor() {
		ConstantUtf8 descriptor = getConstantPoolEntry(descriptorIndex);
		return descriptor.getValue();
	}

	public int getDescriptorIndex() {
		return descriptorIndex;
	}

	@Override
	public void addDependentClassNames(Set<String> classNames) {
		ConstantUtf8 descriptor = getConstantPoolEntry(descriptorIndex);
		if (FieldType.isFieldDescriptor(descriptor.getValue())) {
			new FieldType(descriptor.getValue()).addDependentClassNames(classNames);
		} else if (MethodType.isMethodDescriptor(descriptor.getValue())) {
			new MethodType(descriptor.getValue()).addDependentClassNames(classNames);
		}
	}
}
