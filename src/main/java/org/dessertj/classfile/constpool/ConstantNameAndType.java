package org.dessertj.classfile.constpool;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2024 Hans Jörg Heßmann
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

import java.util.Set;

class ConstantNameAndType extends ConstantPoolEntry {
	public static final int TAG = 12;
	private final int nameIndex;
	private final int descriptorIndex;

	public ConstantNameAndType(int nameIndex, int descriptorIndex) {
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
	}

	@Override
	String dump() {
		return dump(index(nameIndex) + ":" + index(descriptorIndex),
				getName() + ": " + getDescriptor());
	}

	public String getName() {
		ConstantUtf8 name = getConstantPoolEntry(nameIndex);
		return name.getValue();
	}

	public String getDescriptor() {
		ConstantUtf8 descriptor = getConstantPoolEntry(descriptorIndex);
		return descriptor.getValue();
	}

	public int getDescriptorIndex() {
		return descriptorIndex;
	}

	@Override
	public void addDependentClassNames(Set<String> classNames) {
		ConstantUtf8 descriptor = getConstantPoolEntry(descriptorIndex);
		if (FieldType.isFieldDescriptor(descriptor.getValue())) {
			new FieldType(descriptor.getValue()).addDependentClassNames(classNames);
		} else if (MethodType.isMethodDescriptor(descriptor.getValue())) {
			new MethodType(descriptor.getValue()).addDependentClassNames(classNames);
		}
	}
}
