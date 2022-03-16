package de.spricom.dessert.classfile.constpool;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2022 Hans Jörg Heßmann
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
import java.util.Set;

class ConstantFieldref extends ConstantPoolEntry {
	public static final int TAG = 9;
	private final int classIndex;
	private final int nameAndTypeIndex;
	private FieldType type;

	public ConstantFieldref(int classIndex, int nameAndTypeIndex) {
		this.classIndex = classIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	@Override
	void recordReferences(BitSet references) {
		references.set(classIndex);
		references.set(nameAndTypeIndex);
	}

	@Override
	public String dump() {
		return dump(index(classIndex) + "." + index(nameAndTypeIndex),
				getClassName() + "." + getFieldName() + ": " + getFieldType());
	}

	public String getClassName() {
		ConstantClass clazz = getConstantPoolEntry(classIndex);
		return clazz.getName();
	}

	public String getFieldName() {
		ConstantNameAndType nameAndType = getConstantPoolEntry(nameAndTypeIndex);
		return nameAndType.getName();
	}

	public FieldType getFieldType() {
		if (type == null) {
			ConstantNameAndType nameAndType = getConstantPoolEntry(nameAndTypeIndex);
			type = new FieldType(nameAndType.getDescriptor());
		}
		return type;
	}

	@Override
	public void addDependentClassNames(Set<String> classNames) {
		getFieldType().addDependentClassNames(classNames);
	}
}
