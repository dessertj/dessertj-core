package de.spricom.dessert.classfile.constpool;

import java.util.Set;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.classfile.MethodType;

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
	protected void addClassNames(Set<String> classNames, ClassFile cf) {
		ConstantNameAndType nameAndType = (ConstantNameAndType) cf.getConstantPoolEntry(nameAndTypeIndex);
		ConstantUtf8 descriptor = (ConstantUtf8) cf.getConstantPoolEntry(nameAndType.getDescriptorIndex());
		new MethodType(descriptor.getValue()).addDependendClassNames(classNames);
	}

}
