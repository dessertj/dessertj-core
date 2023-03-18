package org.dessertj.classfile;

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

import org.dessertj.classfile.constpool.FieldType;

import java.util.Set;

/**
 * Represents a field within a java class.
 */
public class FieldInfo extends MemberInfo {
	public static final int ACC_VOLATILE = 0x0040; // Declared volatile; cannot
													// be cached.
	public static final int ACC_TRANSIENT = 0x0080; // Declared transient; not
													// written or read by a
													// persistent object
													// manager.
	public static final int ACC_SYNTHETIC = 0x1000; // Declared synthetic; not
													// present in the source
													// code.
	public static final int ACC_ENUM = 0x4000; // Declared as an element of an
												// enum.

	private FieldType fieldType;

	public FieldType getFieldType() {
		if (fieldType == null) {
			fieldType = new FieldType(getDescriptor());
		}
		return fieldType;
	}

	public void addDependentClassNames(Set<String> classNames) {
		getFieldType().addDependentClassNames(classNames);
		super.addDependentClassNames(classNames);
	}

	public String getDeclaration() {
		StringBuilder sb = getDeclarationStringBuilder();
		if (is(ACC_VOLATILE)) {
			sb.append("volatile ");
		}
		if (is(ACC_TRANSIENT)) {
			sb.append("transient ");
		}
		if (is(ACC_SYNTHETIC)) {
			sb.append("synthetic ");
		}
		if (is(ACC_ENUM)) {
			sb.append("enum ");
		}
		sb.append(getFieldType().getDeclaration());
		sb.append(" ");
		sb.append(getName());
		sb.append(";");
		return sb.toString();
	}
}
