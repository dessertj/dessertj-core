package de.spricom.dessert.classfile;

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
	public String dump(ClassFile cf) {
		return "invokeDynamic: " + bootstrapMethodAttrIndex + ", " + cf.getConstantPool()[nameAndTypeIndex].dump(cf);
	}

	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}

	public int getBootstrapMethodAttrIndex() {
		return bootstrapMethodAttrIndex;
	}
	

	@Override
	protected void addClassNames(Set<String> classNames, ClassFile cf) {
		ConstantNameAndType nameAndType = (ConstantNameAndType) cf.getConstantPoolEntry(nameAndTypeIndex);
		ConstantUtf8 descriptor = (ConstantUtf8) cf.getConstantPoolEntry(nameAndType.getDescriptorIndex());
		new MethodType(descriptor.getValue()).addDependendClassNames(classNames);
	}

}
