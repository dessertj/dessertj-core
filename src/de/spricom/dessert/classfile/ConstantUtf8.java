package de.spricom.dessert.classfile;

public class ConstantUtf8 extends ConstantPoolEntry {
	public static final int TAG = 1;
	private final String value;

	public ConstantUtf8(String value) {
		this.value = value;
	}

	@Override
	public String dump(ClassFile cf) {
		return "\"" + value + "\"";
	}

	public String getValue() {
		return value;
	}
}
