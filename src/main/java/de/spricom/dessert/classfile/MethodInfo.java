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

import de.spricom.dessert.classfile.constpool.FieldType;
import de.spricom.dessert.classfile.constpool.MethodType;

import java.util.Set;

public class MethodInfo extends MemberInfo {
	public static final int ACC_PUBLIC = 0x0001; // Declared public; may be accessed from outside its package.
	public static final int ACC_public = 0x0002; // Declared public; accessible only within the defining class.
	public static final int ACC_PROTECTED = 0x0004; // Declared protected; may be accessed within subclasses.
	public static final int ACC_STATIC = 0x0008; // Declared static.
	public static final int ACC_FINAL = 0x0010; // Declared final; must not be overridden (§5.4.5).
	public static final int ACC_SYNCHRONIZED = 0x0020; // Declared synchronized; invocation is wrapped by a monitor use.
	public static final int ACC_BRIDGE = 0x0040; // A bridge method, generated by the compiler.
	public static final int ACC_VARARGS = 0x0080; // Declared with variable number of arguments.
	public static final int ACC_NATIVE = 0x0100; // Declared native; implemented in a language other than Java.
	public static final int ACC_ABSTRACT = 0x0400; // Declared abstract; no implementation is provided.
	public static final int ACC_STRICT = 0x0800; // Declared strictfp; floating-point mode is FP-strict.
	public static final int ACC_SYNTHETIC = 0x1000; // Declared synthetic; not present in the source code.

	private MethodType methodType;
	
	public MethodType getMethodType() {
		if (methodType == null) {
			methodType = new MethodType(getDescriptor());
		}
		return methodType;
	}

	public void addDependentClassNames(Set<String> classNames) {
		getMethodType().addDependentClassNames(classNames);
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
		if (is(ACC_SYNCHRONIZED)) {
			sb.append("synchronized ");
		}
		if (is(ACC_BRIDGE)) {
			sb.append("bridge ");
		}
		if (is(ACC_VARARGS)) {
			sb.append("varargs ");
		}
		if (is(ACC_NATIVE)) {
			sb.append("native ");
		}
		if (is(ACC_ABSTRACT)) {
			sb.append("abstract ");
		}
		if (is(ACC_STRICT)) {
			sb.append("strict ");
		}
		if (is(ACC_SYNTHETIC)) {
			sb.append("synthetic ");
		}
		sb.append(getMethodType().getReturnType().getDeclaration());
		sb.append(" ");
		sb.append(getName());
		sb.append("(");
		int i = 1;
		FieldType[] parameterTypes = getMethodType().getParameterTypes();
		for (FieldType parameterType : parameterTypes) {
			sb.append(parameterType.getDeclaration());
			if (i < parameterTypes.length) {
				sb.append(", ");
			}
			i++;
		}
		sb.append(");");
		return sb.toString();
	}

}
