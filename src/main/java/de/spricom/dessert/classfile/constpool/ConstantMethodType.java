package de.spricom.dessert.classfile.constpool;

import java.util.BitSet;
import java.util.Set;

class ConstantMethodType extends ConstantPoolEntry {
	public static final int TAG = 16;
	private final int descriptorIndex;

	public ConstantMethodType(int referenceIndex) {
		this.descriptorIndex = referenceIndex;
	}

	@Override
	void recordReferences(BitSet references) {
		references.set(descriptorIndex);
	}

	@Override
	public String dump() {
		return "methodType: " + getConstantPoolEntry(descriptorIndex).dump();
	}

	public int getDescriptorIndex() {
		return descriptorIndex;
	}

	@Override
	public void addDependentClassNames(Set<String> classNames) {
		ConstantUtf8 descriptor = getConstantPoolEntry(descriptorIndex);
		new MethodType(descriptor.getValue()).addDependentClassNames(classNames);
	}
}
