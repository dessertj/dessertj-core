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

import java.util.BitSet;

class ConstantModule extends ConstantPoolEntry {
	public static final int TAG = 19;
	private final int nameIndex;

	public ConstantModule(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	@Override
	void recordReferences(BitSet references) {
		references.set(nameIndex);
	}

	@Override
	public String dump() {
		return dump(index(nameIndex), getName());
	}

	public String getName() {
		ConstantUtf8 name = getConstantPoolEntry(nameIndex);
		return name.getValue();
	}
}
