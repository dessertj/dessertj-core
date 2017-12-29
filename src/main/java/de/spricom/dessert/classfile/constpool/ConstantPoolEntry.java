package de.spricom.dessert.classfile.constpool;

import java.util.Set;

import de.spricom.dessert.classfile.dependency.DependencyHolder;

abstract class ConstantPoolEntry implements DependencyHolder {
	private ConstantPool constantPool;
	
	public abstract String dump();
	
	public void addDependentClassNames(Set<String> classNames) {
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
