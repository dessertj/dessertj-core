package de.spricom.dessert.classfile.constpool;

import java.util.Set;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.classfile.FieldType;

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
	protected void addClassNames(Set<String> classNames, ClassFile cf) {
		ConstantNameAndType nameAndType = (ConstantNameAndType) cf.getConstantPoolEntry(nameAndTypeIndex);
		ConstantUtf8 descriptor = (ConstantUtf8) cf.getConstantPoolEntry(nameAndType.getDescriptorIndex());
		new FieldType(descriptor.getValue()).addDependendClassNames(classNames);
	}
}