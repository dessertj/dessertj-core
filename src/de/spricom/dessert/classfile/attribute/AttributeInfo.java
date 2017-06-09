package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

import de.spricom.dessert.classfile.constpool.ConstantPool;
import de.spricom.dessert.classfile.constpool.ConstantUtf8;

public abstract class AttributeInfo {
    private final String name;

	public static AttributeInfo[] readAttributes(DataInputStream is, ConstantPool constantPool) throws IOException {
		AttributeInfo[] attributes = new AttributeInfo[is.readUnsignedShort()];
		for (int i = 0; i < attributes.length; i++) {
			ConstantUtf8 name = (ConstantUtf8) constantPool.getEntry(is.readUnsignedShort());
			switch (name.getValue()) {
			case "ConstantValue":
				attributes[i] = new ConstantValueAttribute(name, is, constantPool.getEntries());
				break;
			case "Code":
				attributes[i] = new CodeAttribute(name, is, constantPool);
				break;
			case "RuntimeVisibleAnnotations":
				attributes[i] = new RuntimeVisibleAnnotationsAttribute(name, is, constantPool.getEntries());
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
