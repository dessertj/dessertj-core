package de.spricom.dessert.classfile.constpool;

import java.util.BitSet;
import java.util.Set;

class ConstantDynamic extends ConstantPoolEntry {
	public static final int TAG = 17;
	private final int bootstrapMethodAttrIndex;
	private final int nameAndTypeIndex;
	private MethodType type;

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
		return dump("[" + bootstrapMethodAttrIndex + "]" + "." + index(nameAndTypeIndex),
				"[bootstrapMethodAttrIndex=" + bootstrapMethodAttrIndex + "]"
						+ "." + getMethodName() + ": " + getMethodType());
	}

	public int getBootstrapMethodAttrIndex() {
		return bootstrapMethodAttrIndex;
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
