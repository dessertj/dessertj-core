package de.spricom.dessert.test.resolve;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.resolve.ClassEntry;
import de.spricom.dessert.resolve.ClassPackage;

import java.io.IOException;

public class FakeClassEntry extends ClassEntry {

    FakeClassEntry(String classname, ClassPackage pckg) {
        super(classname, pckg);
    }

    @Override
    protected ClassFile resolveClassFile() {
        try {
            return new ClassFile(this.getClass());
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to resolve " + FakeClassEntry.class.getSimpleName(), ex);
        }
    }
}
