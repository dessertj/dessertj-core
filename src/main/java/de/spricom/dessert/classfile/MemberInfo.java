package de.spricom.dessert.classfile;

/*-
 * #%L
 * Dessert Dependency Assertion Library
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

import de.spricom.dessert.classfile.attribute.AttributeInfo;
import de.spricom.dessert.classfile.dependency.DependencyHolder;

import java.util.Set;

abstract class MemberInfo implements DependencyHolder {
	private int accessFlags;
	private String name;
	private String descriptor;
	private AttributeInfo[] attributes;

	protected boolean is(int accessFlag) {
		return (accessFlags & accessFlag) == accessFlag;
	}

	public void addDependentClassNames(Set<String> classNames) {
		for (AttributeInfo attribute : attributes) {
			attribute.addDependentClassNames(classNames);
		}
	}

	public abstract String getDeclaration();

	public String toString() {
		return getDeclaration();
	}

	public int getAccessFlags() {
		return accessFlags;
	}

	public void setAccessFlags(int accessFlags) {
		this.accessFlags = accessFlags;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public AttributeInfo[] getAttributes() {
		return attributes;
	}

	public void setAttributes(AttributeInfo[] attributes) {
		this.attributes = attributes;
	}
}
