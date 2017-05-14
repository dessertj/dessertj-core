package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;

import de.spricom.dessert.classfile.ConstantPoolEntry;
import de.spricom.dessert.classfile.ConstantUtf8;

public class ConstantValueAttribute extends AttributeInfo {
    private ConstantPoolEntry contstantValue;

    public ConstantValueAttribute(ConstantUtf8 name, DataInputStream is, ConstantPoolEntry[] constantPoolEntries) throws IOException {
		super(name.getValue());
		checkAttributeLength(is, 2, name.getValue());
		contstantValue = constantPoolEntries[is.readUnsignedShort()];
	}

	public ConstantPoolEntry getContstantValue() {
        return contstantValue;
    }

    public void setContstantValue(ConstantPoolEntry contstantValue) {
        this.contstantValue = contstantValue;
    }
}
