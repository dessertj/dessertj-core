package de.spricom.dessert.resolve;

import de.spricom.dessert.classfile.ClassFile;

public class ClassFileEntry {
    private final ClassContainer pckg;
    private final String filename;
    private final ClassFile classfile;

    public ClassFileEntry(ClassContainer pckg, String filename, ClassFile classfile) {
        this.pckg = pckg;
        this.filename = filename;
        this.classfile = classfile;
    }

    public ClassContainer getPackage() {
        return pckg;
    }

    public ClassFile getClassfile() {
        return classfile;
    }

    public String getFilename() {
        return filename;
    }
}
