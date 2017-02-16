package de.spricom.dessert.classfile;

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
}
