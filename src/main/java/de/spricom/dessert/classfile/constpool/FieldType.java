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

import de.spricom.dessert.classfile.dependency.DependencyHolder;

import java.util.Set;
import java.util.regex.Pattern;

public class FieldType  implements DependencyHolder {
	private static final Pattern DESCRIPTOR_PATTERN = Pattern.compile("\\[*([BCDFIJSZ]|L\\S+;)");

	private Class<?> primitiveType;
	private String objectTypeClassname;
	private int arrayDimensions;

	public FieldType(String descriptor) {
		int i = 0;
		while (descriptor.charAt(i) == '[') {
			arrayDimensions++;
			i++;
		}
		switch (descriptor.charAt(i)) {
		case 'B':
			primitiveType = Byte.TYPE;
			break;
		case 'C':
			primitiveType = Character.TYPE;
			break;
		case 'D':
			primitiveType = Double.TYPE;
			break;
		case 'F':
			primitiveType = Float.TYPE;
			break;
		case 'I':
			primitiveType = Integer.TYPE;
			break;
		case 'J':
			primitiveType = Long.TYPE;
			break;
		case 'L':
			objectTypeClassname = descriptor
					.substring(i + 1, descriptor.indexOf(';', i + 1))
					.replace('/', '.');
			break;
		case 'S':
			primitiveType = Short.TYPE;
			break;
		case 'V':
			primitiveType = Void.TYPE;
			break;
		case 'Z':
			primitiveType = Boolean.TYPE;
			break;
		default:
			throw new IllegalArgumentException("Invalid field descriptor: " + descriptor);
		}
	}

	public int getDescriptorLength() {
		if (isPrimitiveType()) {
			return arrayDimensions + 1;
		} else {
			return arrayDimensions + objectTypeClassname.length() + 2;
		}
	}

	public boolean isArrayType() {
		return arrayDimensions > 0;
	}

	public boolean isPrimitiveType() {
		return primitiveType != null && !isVoidType();
	}

	public boolean isVoidType() {
		return Void.TYPE == primitiveType;
	}

	public boolean isObjectType() {
		return primitiveType == null;
	}

	public String getDeclaration() {
		StringBuilder sb = new StringBuilder();
		if (primitiveType != null) {
			sb.append(primitiveType.getName());
		} else {
			sb.append(objectTypeClassname);
		}
		for (int i = 0; i < arrayDimensions; i++) {
			sb.append("[]");
		}
		return sb.toString();
	}
	
	public final void addDependentClassNames(Set<String> classNames) {
		if (isObjectType()) {
			classNames.add(getObjectTypeClassname());
		}
	}

	public static boolean isFieldDescriptor(String descriptor) {
		return DESCRIPTOR_PATTERN.matcher(descriptor).matches();
	}

	public String toString() {
		return getDeclaration();
	}

	public String getObjectTypeClassname() {
		return objectTypeClassname;
	}

	public int getArrayDimensions() {
		return arrayDimensions;
	}

	public Class<?> getPrimitiveType() {
		return primitiveType;
	}
}
