package org.dessertj.classfile.constpool;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2024 Hans Jörg Heßmann
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

class ConstantUtf8 extends ConstantPoolEntry implements ConstantValue<String> {
	public static final int TAG = 1;
	private final String value;

	public ConstantUtf8(String value) {
		this.value = value;
	}

	@Override
	public String dump() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			int c = value.charAt(i);
			if (c == '\n') {
				sb.append("\\n\n");
			} else if (c == '\r') {
					sb.append("\\r");
			} else if (c == '\t') {
				sb.append("\\t");
			} else if (c == '\f') {
				sb.append("\\f");
			} else if (c == '\\') {
				sb.append("\\\\");
			} else if (Character.isISOControl(c) || !Character.isDefined(c)) {
				sb.append(String.format("\\u%04d", c));
			} else {
				sb.append((char)c);
			}
		}
		return sb.toString();
	}

	public String getValue() {
		return value;
	}
}
