package org.dessertj.classfile.attribute;

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

import org.dessertj.classfile.constpool.ConstantPool;
import org.dessertj.classfile.dependency.DependencyHolder;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

public class ElementValuePair  implements DependencyHolder {
	private final String name;
	private final ElementValue value;
	
	public ElementValuePair(DataInputStream is, ConstantPool constantPool) throws IOException {
		name = constantPool.getUtf8String(is.readUnsignedShort());
		value = new ElementValue(is, constantPool);
	}

	public void addDependentClassNames(Set<String> classNames) {
		value.addDependentClassNames(classNames);
	}

	public String getName() {
		return name;
	}

	public ElementValue getValue() {
		return value;
	}

	public String toString() {
		return name + "=" + value;
	}
}
