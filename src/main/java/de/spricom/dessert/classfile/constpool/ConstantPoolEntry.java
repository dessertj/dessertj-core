package de.spricom.dessert.classfile.constpool;

import de.spricom.dessert.classfile.dependency.DependencyHolder;

import java.util.BitSet;
import java.util.Set;

abstract class ConstantPoolEntry implements DependencyHolder {
	private ConstantPool constantPool;

	void recordReferences(BitSet references) {
	}

	<T extends ConstantPoolEntry> T getConstantPoolEntry(int index) {
		return constantPool.getConstantPoolEntry(index);
	}

	public void addDependentClassNames(Set<String> classNames) {
	}

	public String toString() {
		return typeName() + " " + dump();
	}

	String typeName() {
		return getClass().getSimpleName().substring("Constant".length());
	}

	abstract String dump();

	String dump(String content, String comment) {
		return String.format("%-16s// %s", content, comment);
	}

	static String index(int index) {
		return "#" + index;
	}

	ConstantPool getConstantPool() {
		return constantPool;
	}

	void setConstantPool(ConstantPool constantPool) {
		this.constantPool = constantPool;
	}
}
