package de.spricom.dessert.classfile;

import java.util.Set;

public abstract class ConstantPoolEntry {
	private Set<String> classNamesDone;
	
	public abstract String dump(ClassFile cf);
	
	public final void addDependendClassNames(Set<String> classNames, ClassFile cf) {
		if (classNamesDone == classNames) {
			return;
		}
		classNamesDone = classNames;
		addClassNames(classNames, cf);
	}
	
	protected void addClassNames(Set<String> classNames, ClassFile cf) {
	}
}
