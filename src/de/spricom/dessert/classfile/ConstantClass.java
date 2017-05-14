package de.spricom.dessert.classfile;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstantClass extends ConstantPoolEntry {
	public static final int TAG = 7;
	private static final Pattern classArrayPattern = Pattern.compile("\\[+L(.*);");
	private final int nameIndex;

	public ConstantClass(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	@Override
	public String dump(ClassFile cf) {
		return "class: " + cf.getConstantPool()[nameIndex].dump(cf);
	}

	public int getNameIndex() {
		return nameIndex;
	}

	public String getName(ClassFile cf) {
		ConstantUtf8 utf8 = (ConstantUtf8) cf.getConstantPool()[nameIndex];
		return utf8.getValue().replace('/', '.');
	}
	
	@Override
	protected void addClassNames(Set<String> classNames, ClassFile cf) {
		String name = getName(cf);
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
		int pos = classname.indexOf('$');
		if (pos != -1) {
			classname = classname.substring(0, pos);
		}
		classNames.add(classname);
	}
}
