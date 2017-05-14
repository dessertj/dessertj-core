package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;

import de.spricom.dessert.classfile.ConstantUtf8;

public class UnknownAttribute extends AttributeInfo {
	private final byte[] bytes;

	public UnknownAttribute(ConstantUtf8 name, DataInputStream is) throws IOException {
		super(name.getValue());
		bytes = new byte[is.readInt()];
		is.readFully(bytes);
	}

	public byte[] getBytes() {
		return bytes;
	}
}
