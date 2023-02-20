package de.spricom.dessert.classfile.constpool;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
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

class ConstantDouble extends ConstantPoolEntry implements ConstantValue<Double> {
	public static final int TAG = 6;
	private final double value;

	public ConstantDouble(double value) {
		this.value = value;
	}

	@Override
	public String dump() {
		return Double.toString(value);
	}

	public Double getValue() {
		return value;
	}
}
