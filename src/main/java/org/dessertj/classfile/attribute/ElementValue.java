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
import org.dessertj.classfile.constpool.ConstantValue;
import org.dessertj.classfile.constpool.FieldType;
import org.dessertj.classfile.constpool.MethodType;
import org.dessertj.classfile.dependency.DependencyHolder;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

public class ElementValue implements DependencyHolder {
	private final char tag;
	private ConstantValue<?> constantValue;
	private FieldType type;
	private Annotation annotation;
	private ElementValue[] values;

	public ElementValue(DataInputStream is, ConstantPool constantPool) throws IOException {
		tag = (char) is.readUnsignedByte();
		switch (tag) {
		case 'B':
		case 'C':
		case 'D':
		case 'F':
		case 'I':
		case 'J':
		case 'S':
		case 'Z':
		case 's':
			constantValue = constantPool.getConstantValue(is.readUnsignedShort());
			break;
		case 'e':
			type = constantPool.getFieldType(is.readUnsignedShort());
			constantValue = constantPool.getConstantValue(is.readUnsignedShort());
			break;
		case 'c':
			type = constantPool.getFieldType(is.readUnsignedShort());
			break;
		case '@':
			annotation = new Annotation(is, constantPool);
			break;
		case '[':
			values = new ElementValue[is.readUnsignedShort()];
			for (int i = 0; i < values.length; i++) {
				values[i] = new ElementValue(is, constantPool);
			}
			break;
		default:
			throw new IllegalArgumentException("Invalid ElementValue tag: " + tag);
		}
	}

	public void addDependentClassNames(Set<String> classNames) {
		if (type != null) {
			type.addDependentClassNames(classNames);
		}
		if (annotation != null) {
			annotation.addDependentClassNames(classNames);
		}
		if (values != null) {
			for (ElementValue value : values) {
				value.addDependentClassNames(classNames);
			}
		}
		if (tag == 's') {
			extractTypeFromString(classNames);
		}
	}

	private void extractTypeFromString(Set<String> classNames) {
		String descriptor = (String) constantValue.getValue();
		if (FieldType.isFieldDescriptor(descriptor)) {
			new FieldType(descriptor).addDependentClassNames(classNames);
		} else if (MethodType.isMethodDescriptor(descriptor)) {
			new MethodType(descriptor).addDependentClassNames(classNames);
		}
	}

	public char getTag() {
		return tag;
	}

	public ConstantValue<?> getConstantValue() {
		return constantValue;
	}

	public FieldType getType() {
		return type;
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public ElementValue[] getValues() {
		return values;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		switch (tag) {
			case 'B':
			case 'D':
			case 'F':
			case 'I':
			case 'J':
			case 'S':
			case 'e':
			case 's':
				sb.append(constantValue.getValue());
				break;
			case 'Z':
				sb.append(Integer.valueOf(1).equals(constantValue.getValue()));
				break;
			case 'C':
				sb.append((char)Integer.parseInt(constantValue.getValue().toString()));
				break;
			case 'c':
				sb.append("class ").append(type);
				break;
			case '@':
				sb.append(annotation);
				break;
			case '[':
				sb.append("[");
				for (ElementValue value : values) {
					if (sb.length() >= 2) {
						sb.append(", ");
					}
					sb.append(value);
				}
				sb.append("]");
				break;
			default:
				throw new IllegalArgumentException("Invalid ElementValue tag: " + tag);
		}
		return sb.toString();
	}
}
