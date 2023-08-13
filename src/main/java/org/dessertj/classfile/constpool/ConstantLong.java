package org.dessertj.classfile.constpool;

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

class ConstantLong extends ConstantPoolEntry  implements ConstantValue<Long> {
	public static final int TAG = 5;
	private final long value;

	public ConstantLong(long value) {
		this.value = value;
	}

	@Override
	public String dump() {
		return Long.toString(value);
	}

	public Long getValue() {
		return value;
	}
}