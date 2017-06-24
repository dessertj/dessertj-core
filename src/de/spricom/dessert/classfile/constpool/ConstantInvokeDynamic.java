package de.spricom.dessert.classfile.constpool;

import java.util.Set;

class ConstantInvokeDynamic extends ConstantPoolEntry {
	public static final int TAG = 18;
	private final int bootstrapMethodAttrIndex;
	private final int nameAndTypeIndex;

	public ConstantInvokeDynamic(int bootstrapMethodAttrIndex, int referenceIndex) {
		this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
		this.nameAndTypeIndex = referenceIndex;
	}

	@Override
	public String dump() {
		return "invokeDynamic: " + bootstrapMethodAttrIndex + ", " + getConstantPoolEntry(nameAndTypeIndex).dump();
	}

	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}

	public int getBootstrapMethodAttrIndex() {
		return bootstrapMethodAttrIndex;
	}
	

	@Override
	public void addDependentClassNames(Set<String> classNames) {
		ConstantNameAndType nameAndType = (ConstantNameAndType) getConstantPoolEntry(nameAndTypeIndex);
		ConstantUtf8 descriptor = (ConstantUtf8)getConstantPoolEntry(nameAndType.getDescriptorIndex());
		new MethodType(descriptor.getValue()).addDependentClassNames(classNames);
	}

}
