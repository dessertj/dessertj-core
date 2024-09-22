package org.dessertj.classfile.attribute;

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

import org.dessertj.classfile.constpool.ConstantPool;
import org.dessertj.classfile.constpool.FieldType;
import org.dessertj.classfile.dependency.DependencyHolder;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

public class Annotation implements DependencyHolder {
	private final FieldType type;
	private final ElementValuePair[] elementValuePairs;

	public Annotation(DataInputStream is, ConstantPool constantPool) throws IOException {
		type = constantPool.getFieldType(is.readUnsignedShort());
		elementValuePairs = new ElementValuePair[is.readUnsignedShort()];
		for (int i = 0; i < elementValuePairs.length; i++) {
			elementValuePairs[i] = new ElementValuePair(is, constantPool);
		}
	}

	public FieldType getType() {
		return type;
	}

	public ElementValuePair[] getElementValuePairs() {
		return elementValuePairs;
	}

	public void addDependentClassNames(Set<String> classNames) {
		type.addDependentClassNames(classNames);
		for (ElementValuePair pair : elementValuePairs) {
			pair.addDependentClassNames(classNames);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append("@").append(type.toString()).append("(");
		if (elementValuePairs.length > 0) {
			boolean first = true;
			for (ElementValuePair pair : elementValuePairs) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append(pair);
			}
		}
		sb.append(")");
		return sb.toString();
	}
}
