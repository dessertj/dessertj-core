package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

import de.spricom.dessert.classfile.constpool.ConstantPool;
import de.spricom.dessert.classfile.constpool.MethodType;

public class EnclosingMethodAttribute extends AttributeInfo {
	private final String enclosingClassname;
	private final String enclosingMethodName;
	private final MethodType enclosingMethodType;

	public EnclosingMethodAttribute(String name, DataInputStream is, ConstantPool constantPool) throws IOException {
		super(name);
		if (is.readInt() != 4) {
			// length must be two
			throw new IllegalArgumentException("Unexpected length of EnclosingMethod attribute.");
		}
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
