package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

import de.spricom.dessert.classfile.constpool.ConstantPool;

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
