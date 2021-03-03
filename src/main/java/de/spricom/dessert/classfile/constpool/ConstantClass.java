package de.spricom.dessert.classfile.constpool;

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

import java.util.BitSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ConstantClass extends ConstantPoolEntry {
	public static final int TAG = 7;
	private static final Pattern classArrayPattern = Pattern.compile("\\[+L(.*);");
	private final int nameIndex;

	public ConstantClass(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	@Override
	void recordReferences(BitSet references) {
		references.set(nameIndex);
	}

	@Override
	public String dump() {
		return dump(index(nameIndex), getName());
	}

	public String getPhysicalName() {
		ConstantUtf8 name = getConstantPoolEntry(nameIndex);
		return name.getValue();
	}

	public String getName() {
		return getPhysicalName().replace('/', '.');
	}

	public void addDependentClassNames(Set<String> classNames) {
		String name = getName();
		Matcher matcher = classArrayPattern.matcher(name);
		String classname;
		if (matcher.matches()) {
			classname = matcher.group(1);
		} else if (name.startsWith("[")) {
			// ignore arrays of primitive types
			return;
		} else {
			classname = name;
		}
		classNames.add(classname);
	}
}
