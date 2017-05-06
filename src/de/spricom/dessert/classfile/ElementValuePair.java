package de.spricom.dessert.classfile;

import java.io.DataInputStream;
import java.io.IOException;

public class ElementValuePair {
	private String name;
	private ElementValue value;
	
	public ElementValuePair(DataInputStream is, ConstantPoolEntry[] constantPoolEntries) throws IOException {
		name = ((ConstantUtf8) constantPoolEntries[is.readUnsignedShort()]).getValue();
		value = new ElementValue(is, constantPoolEntries);
	}

}
