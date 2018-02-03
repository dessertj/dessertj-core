package de.spricom.dessert.classfile.attribute;

import de.spricom.dessert.classfile.constpool.ConstantPool;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

public class InnerClassesAttribute extends AttributeInfo {

    private final InnerClass[] innerClasses;

    public InnerClassesAttribute(String name, DataInputStream is, ConstantPool constantPool) throws IOException {
        super(name);
        is.readInt(); // skip length
        innerClasses = new InnerClass[is.readUnsignedShort()];
        for (int i = 0; i < innerClasses.length; i++) {
            innerClasses[i] = new InnerClass(is, constantPool);
        }
    }

    public InnerClass[] getInnerClasses() {
        return innerClasses;
    }

    public void addDependentClassNames(Set<String> classNames) {
        for (InnerClass innerClass : innerClasses) {
            innerClass.addDependentClassNames(classNames);
        }
    }

}
