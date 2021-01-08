package de.spricom.dessert.classfile.constpool;

import java.util.BitSet;
import java.util.Set;

class ConstantFieldref extends ConstantPoolEntry {
	public static final int TAG = 9;
	private final int classIndex;
	private final int nameAndTypeIndex;
	private FieldType type;

	public ConstantFieldref(int classIndex, int nameAndTypeIndex) {
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
				getClassName() + "." + getFieldName() + ": " + getFieldType());
	}

	public String getClassName() {
		ConstantClass clazz = getConstantPoolEntry(classIndex);
		return clazz.getName();
	}

	public String getFieldName() {
		ConstantNameAndType nameAndType = getConstantPoolEntry(nameAndTypeIndex);
		return nameAndType.getName();
	}

	public FieldType getFieldType() {
		if (type == null) {
			ConstantNameAndType nameAndType = getConstantPoolEntry(nameAndTypeIndex);
			type = new FieldType(nameAndType.getDescriptor());
		}
		return type;
	}

	@Override
	public void addDependentClassNames(Set<String> classNames) {
		getFieldType().addDependentClassNames(classNames);
	}
}
