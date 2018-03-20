package de.spricom.dessert.test.resolve;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.resolve.ClassContainer;
import de.spricom.dessert.resolve.ClassFileEntry;

import java.io.IOException;

public class FakeClassFileEntry extends ClassFileEntry {

    FakeClassFileEntry(String classname, ClassContainer pckg) {
        super(classname, pckg);
    }

    @Override
    protected ClassFile resolveClassFile() {
        try {
            return new ClassFile(this.getClass());
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to resolve " + FakeClassFileEntry.class.getSimpleName(), ex);
        }
    }
}
