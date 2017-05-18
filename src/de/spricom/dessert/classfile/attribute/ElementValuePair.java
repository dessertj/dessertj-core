package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

import de.spricom.dessert.classfile.constpool.ConstantPoolEntry;
import de.spricom.dessert.classfile.constpool.ConstantUtf8;

public class ElementValuePair {
	private String name;
	private ElementValue value;
	
	public ElementValuePair(DataInputStream is, ConstantPoolEntry[] constantPoolEntries) throws IOException {
		name = ((ConstantUtf8) constantPoolEntries[is.readUnsignedShort()]).getValue();
		value = new ElementValue(is, constantPoolEntries);
	}

	public void addDependendClassNames(Set<String> classNames) {
		value.addDependendClassNames(classNames);
	}

	public String getName() {
		return name;
	}

	public ElementValue getValue() {
		return value;
	}
}
