package org.dessertj.classfile.attribute;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2023 Hans Jörg Heßmann
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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

public abstract class AbstractTypeAnnotationsAttribute extends AttributeInfo {
	private final TypeAnnotation[] typeAnnotations;

	AbstractTypeAnnotationsAttribute(String name, DataInputStream is, ConstantPool constantPool)
			throws IOException {
		super(name);
		is.readInt(); // skip length
		typeAnnotations = new TypeAnnotation[is.readUnsignedShort()];
		for (int i = 0; i < typeAnnotations.length; i++) {
			typeAnnotations[i] = new TypeAnnotation(is, constantPool);
		}
	}

	public TypeAnnotation[] getTypeAnnotations() {
		return typeAnnotations;
	}

	public void addDependentClassNames(Set<String> classNames) {
		for (TypeAnnotation annotation : typeAnnotations) {
			annotation.addDependentClassNames(classNames);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
        sb.append(":");
		for (TypeAnnotation typeAnnotation : typeAnnotations) {
            sb.append("\n\t\t\t\t");
			sb.append(typeAnnotation);
		}
		return sb.toString();
	}
}
