package de.spricom.dessert.classfile.constpool;

import java.util.Set;

import de.spricom.dessert.classfile.ClassFile;

public abstract class ConstantPoolEntry {
	private ConstantPool constantPool;
	private Set<String> classNamesDone;
	
	public abstract String dump();
	
	public final void addDependendClassNames(Set<String> classNames, ClassFile cf) {
		if (classNamesDone == classNames) {
			return;
		}
		classNamesDone = classNames;
		addClassNames(classNames, cf);
	}
	
	protected void addClassNames(Set<String> classNames, ClassFile cf) {
	}
	
	ConstantPoolEntry getConstantPoolEntry(int index) {
		return constantPool.getEntry(index);
	}
	
	ConstantPool getConstantPool() {
		return constantPool;
	}

	void setConstantPool(ConstantPool constantPool) {
		this.constantPool = constantPool;
	}
}
