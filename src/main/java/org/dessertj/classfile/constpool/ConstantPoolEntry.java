package org.dessertj.classfile.constpool;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2025 Hans Jörg Heßmann
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.dessertj.classfile.dependency.DependencyHolder;

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
