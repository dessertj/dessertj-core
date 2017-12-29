package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;

public class UnknownAttribute extends AttributeInfo {
	private final byte[] bytes;

	public UnknownAttribute(String name, DataInputStream is) throws IOException {
		super(name);
		bytes = new byte[is.readInt()];
		is.readFully(bytes);
	}

	public byte[] getBytes() {
		return bytes;
	}
}
