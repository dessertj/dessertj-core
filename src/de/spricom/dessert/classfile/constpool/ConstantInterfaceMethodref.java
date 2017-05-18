package de.spricom.dessert.classfile.constpool;

import java.util.Set;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.classfile.MethodType;

class ConstantInterfaceMethodref extends ConstantPoolEntry {
	public static final int TAG = 11;
	private final int classIndex;
	private final int nameAndTypeIndex;

	public ConstantInterfaceMethodref(int classIndex, int nameAndTypeIndex) {
		this.classIndex = classIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	@Override
	public String dump(ClassFile cf) {
		return "interface methodref: " + cf.getConstantPool()[classIndex].dump(cf) + ", "
				+ cf.getConstantPool()[nameAndTypeIndex].dump(cf);
	}

	public int getClassIndex() {
		return classIndex;
	}

	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}

	@Override
	protected void addClassNames(Set<String> classNames, ClassFile cf) {
		ConstantNameAndType nameAndType = (ConstantNameAndType) cf.getConstantPoolEntry(nameAndTypeIndex);
		ConstantUtf8 descriptor = (ConstantUtf8) cf.getConstantPoolEntry(nameAndType.getDescriptorIndex());
		new MethodType(descriptor.getValue()).addDependendClassNames(classNames);
	}
}
