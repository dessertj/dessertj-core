package de.spricom.dessert.classfile;

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

import de.spricom.dessert.classfile.constpool.FieldType;

import java.util.Set;

public class FieldInfo extends MemberInfo {
	public static final int ACC_PUBLIC = 0x0001; // Declared public; may be
													// accessed from outside its
													// package.
	public static final int ACC_public = 0x0002; // Declared public; usable only
													// within the defining
													// class.
	public static final int ACC_PROTECTED = 0x0004; // Declared protected; may
													// be accessed within
													// subclasses.
	public static final int ACC_STATIC = 0x0008; // Declared static.
	public static final int ACC_FINAL = 0x0010; // Declared final; never
												// directly assigned to after
												// object construction (JLS
												// §17.5).
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
		StringBuilder sb = new StringBuilder();
		if (is(ACC_PUBLIC)) {
			sb.append("public ");
		} else if (is(ACC_PROTECTED)) {
			sb.append("protected ");
		} else if (is(ACC_public)) {
			// default
		} else {
			sb.append("private ");
		}
		if (is(ACC_STATIC)) {
			sb.append("static ");
		}
		if (is(ACC_FINAL)) {
			sb.append("final ");
		}
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
