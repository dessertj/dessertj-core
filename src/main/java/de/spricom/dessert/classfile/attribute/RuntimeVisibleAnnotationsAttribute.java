package de.spricom.dessert.classfile.attribute;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2021 Hans Jörg Heßmann
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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

public class RuntimeVisibleAnnotationsAttribute extends AttributeInfo {
	private final Annotation[] annotations;

	public RuntimeVisibleAnnotationsAttribute(String name, DataInputStream is, ConstantPool constantPool)
			throws IOException {
		super(name);
		is.readInt(); // skip length
		annotations = new Annotation[is.readUnsignedShort()];
		for (int i = 0; i < annotations.length; i++) {
			annotations[i] = new Annotation(is, constantPool);
		}
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}

	public void addDependentClassNames(Set<String> classNames) {
		for (Annotation annotation : annotations) {
			annotation.addDependentClassNames(classNames);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		boolean first = true;
		for (Annotation annotation : annotations) {
			if (first) {
				sb.append(": ");
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(annotation);
		}
		return sb.toString();
	}
}
