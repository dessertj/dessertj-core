package de.spricom.dessert.classfile.constpool;

class ConstantUtf8 extends ConstantPoolEntry implements ConstantValue<String> {
	public static final int TAG = 1;
	private final String value;

	public ConstantUtf8(String value) {
		this.value = value;
	}

	@Override
	public String dump() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			int c = value.charAt(i);
			if (c == '\n') {
				sb.append("\\n\n");
			} else if (c == '\r') {
					sb.append("\\r");
			} else if (c == '\t') {
				sb.append("\\t");
			} else if (c == '\f') {
				sb.append("\\f");
			} else if (c == '\\') {
				sb.append("\\\\");
			} else if (Character.isISOControl(c) || !Character.isDefined(c)) {
				sb.append(String.format("\\u%04d", c));
			} else {
				sb.append((char)c);
			}
		}
		return sb.toString();
	}

	public String getValue() {
		return value;
	}
}
