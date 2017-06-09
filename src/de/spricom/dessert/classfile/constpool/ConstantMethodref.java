package de.spricom.dessert.classfile.constpool;

import java.util.Set;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.classfile.MethodType;

class ConstantMethodref extends ConstantPoolEntry {
	public static final int TAG = 10;
	private final int classIndex;
	private final int nameAndTypeIndex;

	public ConstantMethodref(int classIndex, int nameAndTypeIndex) {
		this.classIndex = classIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	@Override
	public String dump() {
		return "methodref: " + getConstantPoolEntry(classIndex).dump() + ", "
				+ getConstantPoolEntry(nameAndTypeIndex).dump();
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
