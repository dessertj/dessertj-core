package de.spricom.dessert.classfile.constpool;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ConstantClass extends ConstantPoolEntry {
	public static final int TAG = 7;
	private static final Pattern classArrayPattern = Pattern.compile("\\[+L(.*);");
	private final int nameIndex;

	public ConstantClass(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	@Override
	public String dump() {
		return "class: " + getConstantPoolEntry(nameIndex).dump();
	}

	public int getNameIndex() {
		return nameIndex;
	}

	public String getName() {
		return getConstantPool().getUtf8String(nameIndex).replace('/', '.');
	}

	public void addDependentClassNames(Set<String> classNames) {
		String name = getName();
		Matcher matcher = classArrayPattern.matcher(name);
		String classname;
		if (matcher.matches()) {
			classname = matcher.group(1);
		} else if (name.startsWith("[")) {
			// ignore arrays of primitive types
			return;
		} else {
			classname = name;
		}
		classNames.add(classname);
	}
}
