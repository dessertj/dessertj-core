package de.spricom.dessert.resolve;

import de.spricom.dessert.classfile.ClassFile;

public class ClassFileEntry {
    private final ClassContainer pckg;
    private final ClassFile classfile;

    public ClassFileEntry(ClassContainer pckg, ClassFile classfile) {
        this.pckg = pckg;
        this.classfile = classfile;
    }

    public ClassContainer getPackage() {
        return pckg;
    }

    public ClassFile getClassfile() {
        return classfile;
    }
}
