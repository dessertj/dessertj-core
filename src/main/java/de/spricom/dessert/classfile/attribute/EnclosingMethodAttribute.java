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
import de.spricom.dessert.classfile.constpool.MethodType;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

/**
 * Representes a
 * <a href="https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html#jvms-4.7.7" target="_blank">
 * Java Virtual Machine Specification: 4.7.7. The EnclosingMethod Attribute</a>.
 */
public class EnclosingMethodAttribute extends AttributeInfo {
	private final String enclosingClassname;
	private final String enclosingMethodName;
	private final MethodType enclosingMethodType;

	public EnclosingMethodAttribute(String name, DataInputStream is, ConstantPool constantPool) throws IOException {
		super(name);
		skipLength(is);
		enclosingClassname = constantPool.getConstantClassName(is.readUnsignedShort());
		int enclosingMethodIndex = is.readUnsignedShort();
		if (enclosingMethodIndex == 0) {
			enclosingMethodName = null;
			enclosingMethodType = null;
		} else {
			enclosingMethodName = constantPool.getNameAndTypeName(enclosingMethodIndex);
			enclosingMethodType = constantPool.getNameAndTypeMethodType(enclosingMethodIndex);
		}
	}

	@Override
	public void addDependentClassNames(Set<String> classNames) {
		if (enclosingMethodType != null) {
			enclosingMethodType.addDependentClassNames(classNames);
		}
	}

	public String getEnclosingClassname() {
		return enclosingClassname;
	}

	public String getEnclosingMethodName() {
		return enclosingMethodName;
	}

	public MethodType getEnclosingMethodType() {
		return enclosingMethodType;
	}
}
