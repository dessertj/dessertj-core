package de.spricom.dessert.classfile.attribute;

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

import de.spricom.dessert.classfile.constpool.ConstantPool;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

public class SignatureAttribute extends AttributeInfo {

	private final String signature;

	public SignatureAttribute(String name, DataInputStream is, ConstantPool constantPool) throws IOException {
		super(name);
		if (is.readInt() != 2) {
			// length must be two
			throw new IllegalArgumentException("Unexpected length of Signature attribute.");
		}
		signature = constantPool.getUtf8String(is.readUnsignedShort());
	}

	@Override
	public void addDependentClassNames(Set<String> classNames) {
		SignatureParser parser = new SignatureParser(signature, classNames);
		switch (getContext()) {
		case CLASS:
			parser.parseClassSignature();
			break;
		case METHOD:
			parser.parseMethodSignature();
			break;
		case FIELD:
			parser.parseFieldSignature();
			break;
		default:
			throw new IllegalArgumentException("Signature attribute not supported for context " + getContext() + "!");
		}
	}

}
