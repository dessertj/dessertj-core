package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

import de.spricom.dessert.classfile.FieldType;
import de.spricom.dessert.classfile.constpool.ConstantPoolEntry;
import de.spricom.dessert.classfile.constpool.ConstantUtf8;

public class Annotation {
	private FieldType type;
	private ElementValuePair[] elementValuePairs;

	public Annotation(DataInputStream is, ConstantPoolEntry[] constantPoolEntries) throws IOException {
		type = new FieldType(((ConstantUtf8) constantPoolEntries[is.readUnsignedShort()]).getValue());
		elementValuePairs = new ElementValuePair[is.readUnsignedShort()];
		for (int i = 0; i < elementValuePairs.length; i++) {
			elementValuePairs[i] = new ElementValuePair(is, constantPoolEntries);
		}
	}

	public void addDependendClassNames(Set<String> classNames) {
		type.addDependendClassNames(classNames);
		for (ElementValuePair pair : elementValuePairs) {
			pair.addDependendClassNames(classNames);
		}
	}

	public FieldType getType() {
		return type;
	}

	public ElementValuePair[] getElementValuePairs() {
		return elementValuePairs;
	}
}
