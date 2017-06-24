package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

import de.spricom.dessert.classfile.constpool.ConstantPool;
import de.spricom.dessert.classfile.dependency.DependencyHolder;

public class ElementValuePair  implements DependencyHolder {
	private String name;
	private ElementValue value;
	
	public ElementValuePair(DataInputStream is, ConstantPool constantPool) throws IOException {
		name = constantPool.getUtf8String(is.readUnsignedShort());
		value = new ElementValue(is, constantPool);
	}

	public void addDependentClassNames(Set<String> classNames) {
		value.addDependentClassNames(classNames);
	}

	public String getName() {
		return name;
	}

	public ElementValue getValue() {
		return value;
	}
}
