package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;

import de.spricom.dessert.classfile.ConstantPoolEntry;
import de.spricom.dessert.classfile.ConstantUtf8;
import de.spricom.dessert.classfile.FieldType;

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

}
