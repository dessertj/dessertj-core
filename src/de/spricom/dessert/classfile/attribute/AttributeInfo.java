package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

import de.spricom.dessert.classfile.ConstantPoolEntry;
import de.spricom.dessert.classfile.ConstantUtf8;

public abstract class AttributeInfo {
    private final String name;

	public static AttributeInfo[] readAttributes(DataInputStream is, ConstantPoolEntry[] constantPoolEntries) throws IOException {
		AttributeInfo[] attributes = new AttributeInfo[is.readUnsignedShort()];
		for (int i = 0; i < attributes.length; i++) {
			ConstantUtf8 name = (ConstantUtf8) constantPoolEntries[is.readUnsignedShort()];
			switch (name.getValue()) {
			case "ConstantValue":
				attributes[i] = new ConstantValueAttribute(name, is, constantPoolEntries);
				break;
			case "Code":
				attributes[i] = new CodeAttribute(name, is, constantPoolEntries);
				break;
			case "RuntimeVisibleAnnotationsAttribute":
				attributes[i] = new RuntimeVisibleAnnotationsAttribute(name, is, constantPoolEntries);
				break;
			default:
				attributes[i] = new UnknownAttribute(name, is);
			}
		}
		return attributes;
	}

    public AttributeInfo(String name) {
		this.name = name;
	}

	public String getName() {
        return name;
    }
	
	protected final void checkAttributeLength(DataInputStream is, int expectedLength, String name) throws IOException {
		int len;
		if ((len = is.readInt()) != expectedLength) {
			throw new IOException("Unexpected length of " + len + " for attribute " + name);
		}
	}
	
	public void addDependendClassNames(Set<String> classNames) {
	}
}
