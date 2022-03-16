package de.spricom.dessert.classfile.attribute;

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

import de.spricom.dessert.classfile.constpool.ConstantPool;
import de.spricom.dessert.classfile.constpool.FieldType;
import de.spricom.dessert.classfile.dependency.DependencyHolder;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

public class Annotation implements DependencyHolder {
	private FieldType type;
	private ElementValuePair[] elementValuePairs;

	public Annotation(DataInputStream is, ConstantPool constantPool) throws IOException {
		type = constantPool.getFieldType(is.readUnsignedShort());
		elementValuePairs = new ElementValuePair[is.readUnsignedShort()];
		for (int i = 0; i < elementValuePairs.length; i++) {
			elementValuePairs[i] = new ElementValuePair(is, constantPool);
		}
	}

	public void addDependentClassNames(Set<String> classNames) {
		type.addDependentClassNames(classNames);
		for (ElementValuePair pair : elementValuePairs) {
			pair.addDependentClassNames(classNames);
		}
	}

	public FieldType getType() {
		return type;
	}

	public ElementValuePair[] getElementValuePairs() {
		return elementValuePairs;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(type.toString());
		for (ElementValuePair pair : elementValuePairs) {
			sb.append(" ").append(pair);
		}
		return sb.toString();
	}
}
