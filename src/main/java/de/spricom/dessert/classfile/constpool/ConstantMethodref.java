package de.spricom.dessert.classfile.constpool;

import java.util.BitSet;
import java.util.Set;

class ConstantMethodref extends ConstantPoolEntry {
	public static final int TAG = 10;
	private final int classIndex;
	private final int nameAndTypeIndex;
	private MethodType type;

	public ConstantMethodref(int classIndex, int nameAndTypeIndex) {
		this.classIndex = classIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	@Override
	void recordReferences(BitSet references) {
		references.set(classIndex);
		references.set(nameAndTypeIndex);
	}

	@Override
	public String dump() {
		return dump(index(classIndex) + "." + index(nameAndTypeIndex),
				getClassName() + "." + getMethodName() + ": " + getMethodType());
	}

	public String getClassName() {
		ConstantClass clazz = getConstantPoolEntry(classIndex);
		return clazz.getName();
	}

	public String getMethodName() {
		ConstantNameAndType nameAndType = getConstantPoolEntry(nameAndTypeIndex);
		return nameAndType.getName();
	}

	public MethodType getMethodType() {
		if (type == null) {
			ConstantNameAndType nameAndType = getConstantPoolEntry(nameAndTypeIndex);
			type = new MethodType(nameAndType.getDescriptor());
		}
		return type;
	}

	@Override
	public void addDependentClassNames(Set<String> classNames) {
		getMethodType().addDependentClassNames(classNames);
	}
}
