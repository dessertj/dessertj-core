package de.spricom.dessert.classfile.constpool;

import java.util.Set;

import de.spricom.dessert.classfile.ClassFile;

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
