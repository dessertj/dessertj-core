package de.spricom.dessert.classfile.constpool;

import de.spricom.dessert.classfile.dependency.DependencyHolder;

import java.util.BitSet;
import java.util.Set;

abstract class ConstantPoolEntry implements DependencyHolder {
	private ConstantPool constantPool;

	public abstract String dump();
	
	public void addDependentClassNames(Set<String> classNames) {
	}

	<T extends ConstantPoolEntry> T getConstantPoolEntry(int index) {
		return constantPool.getConstantPoolEntry(index);
	}

	String typeName() {
		return getClass().getSimpleName().substring("Constant".length());
	}

	void recordReferences(BitSet references) {
	}

	public String toString() {
		return dump();
	}

	ConstantPool getConstantPool() {
		return constantPool;
	}

	void setConstantPool(ConstantPool constantPool) {
		this.constantPool = constantPool;
	}
}
