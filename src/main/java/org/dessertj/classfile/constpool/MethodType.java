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

import org.dessertj.classfile.dependency.DependencyHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class MethodType  implements DependencyHolder {
	private static final Pattern DESCRIPTOR_PATTERN = Pattern
			.compile("\\((\\[*([BCDFIJSZ]|L\\S+;))*\\)\\[*([BCDFIJSVZ]|L\\S+;)");

	private final FieldType[] parameterTypes;
	private final FieldType returnType;
	
	public MethodType(String descriptor) {
		assert descriptor != null : "descriptor == null";
		if ('(' != descriptor.charAt(0)) {
			throw new IllegalArgumentException("Invalid method descriptor: " + descriptor);
		}
		List<FieldType> params = new ArrayList<FieldType>();
		int index = 1;
		while (')' != descriptor.charAt(index)) {
			FieldType param = new FieldType(descriptor.substring(index));
			params.add(param);
			index += param.getDescriptorLength();
		}
		parameterTypes = params.toArray(new FieldType[0]);
		index++;
		returnType = new FieldType(descriptor.substring(index));
	}

	public final void addDependentClassNames(Set<String> classNames) {
		for (FieldType parameterType : parameterTypes) {
			parameterType.addDependentClassNames(classNames);
		}
		returnType.addDependentClassNames(classNames);
	}

	public static boolean isMethodDescriptor(String descriptor) {
		return DESCRIPTOR_PATTERN.matcher(descriptor).matches();
	}

	public FieldType[] getParameterTypes() {
		return parameterTypes;
	}

	public FieldType getReturnType() {
		return returnType;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		for (FieldType parameterType : parameterTypes) {
			if (sb.length() > 1) {
				sb.append(", ");
			}
			sb.append(parameterType);
		}
		sb.append(") -> ");
		sb.append(returnType);
		return sb.toString();
	}
}
