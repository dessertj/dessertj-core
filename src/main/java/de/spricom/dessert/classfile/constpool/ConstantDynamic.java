package de.spricom.dessert.classfile.constpool;

import java.util.BitSet;
import java.util.Set;

class ConstantDynamic extends ConstantPoolEntry {
	public static final int TAG = 17;
	private final int bootstrapMethodAttrIndex;
	private final int nameAndTypeIndex;

	public ConstantDynamic(int bootstrapMethodAttrIndex, int referenceIndex) {
		this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
		this.nameAndTypeIndex = referenceIndex;
	}

	@Override
	void recordReferences(BitSet references) {
		references.set(nameAndTypeIndex);
	}

	@Override
	public String dump() {
		return "bootstrapMethodAttrIndex " + bootstrapMethodAttrIndex
				+ ": " + getConstantPoolEntry(nameAndTypeIndex).dump();
	}

	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}

	public int getBootstrapMethodAttrIndex() {
		return bootstrapMethodAttrIndex;
	}
	

	@Override
	public void addDependentClassNames(Set<String> classNames) {
		ConstantNameAndType nameAndType = getConstantPoolEntry(nameAndTypeIndex);
		ConstantUtf8 descriptor = getConstantPoolEntry(nameAndType.getDescriptorIndex());
		new MethodType(descriptor.getValue()).addDependentClassNames(classNames);
	}

}
