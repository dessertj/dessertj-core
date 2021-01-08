package de.spricom.dessert.classfile.constpool;

class ConstantLong extends ConstantPoolEntry  implements ConstantValue<Long> {
	public static final int TAG = 5;
	private final long value;

	public ConstantLong(long value) {
		this.value = value;
	}

	@Override
	public String dump() {
		return Long.toString(value);
	}

	public Long getValue() {
		return value;
	}
}
