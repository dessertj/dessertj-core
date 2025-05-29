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
import java.util.Set;

class ConstantInvokeDynamic extends ConstantPoolEntry  {
	public static final int TAG = 18;

	private final int bootstrapMethodAttrIndex;
	private final int nameAndTypeIndex;
	private MethodType type;

	public ConstantInvokeDynamic(int bootstrapMethodAttrIndex, int referenceIndex) {
		this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
		this.nameAndTypeIndex = referenceIndex;
	}

	@Override
	void recordReferences(BitSet references) {
		references.set(nameAndTypeIndex);
	}

	@Override
	public String dump() {
		return dump("[" + bootstrapMethodAttrIndex + "]" + "." + index(nameAndTypeIndex),
				"[bootstrapMethodAttrIndex=" + bootstrapMethodAttrIndex + "]"
						+ "." + getMethodName() + ": " + getMethodType());
	}

	public int getBootstrapMethodAttrIndex() {
		return bootstrapMethodAttrIndex;
	}

	public String getMethodName() {
		ConstantNameAndType nameAndType = getConstantPoolEntry(nameAndTypeIndex);
		return nameAndType.getName();
	}

	public MethodType getMethodType() {
		if (type == null) {
			ConstantNameAndType nameAndType = getConstantPoolEntry(nameAndTypeIndex);
			type = new MethodType(nameAndType.getDescriptor());
		}
		return type;
	}

	@Override
	public void addDependentClassNames(Set<String> classNames) {
		getMethodType().addDependentClassNames(classNames);
	}
}
