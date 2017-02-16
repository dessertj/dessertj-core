package de.spricom.dessert.classfile;

public class ConstantValueAttribute extends AttributeInfo {
    private ConstantPoolEntry contstantValue;

    public ConstantPoolEntry getContstantValue() {
        return contstantValue;
    }

    public void setContstantValue(ConstantPoolEntry contstantValue) {
        this.contstantValue = contstantValue;
    }
}
