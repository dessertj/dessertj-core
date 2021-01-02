package de.spricom.dessert.classfile.constpool;

class ConstantModule extends ConstantPoolEntry implements ConstantValue<String>  {
	public static final int TAG = 19;
	private final int nameIndex;

	public ConstantModule(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	@Override
	public String dump() {
		return "name: " + getConstantPoolEntry(nameIndex).dump();
	}

	public int getNameIndex() {
		return nameIndex;
	}

	@Override
	public String getValue() {
		return getConstantPool().getUtf8String(nameIndex);
	}
}
