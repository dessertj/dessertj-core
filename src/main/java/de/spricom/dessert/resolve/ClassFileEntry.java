package de.spricom.dessert.resolve;

import de.spricom.dessert.classfile.ClassFile;

import java.util.LinkedList;
import java.util.List;

public class ClassFileEntry {
    private final ClassContainer pckg;
    private final String filename;
    private final ClassFile classfile;
    private List<ClassFileEntry> alternatives;

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

    public List<ClassFileEntry> getAlternatives() {
        return alternatives;
    }

    void addAlternative(ClassFileEntry alt) {
        assert alt.alternatives == null : "alt.alternatives != null";
        if (alternatives == null) {
            alternatives = new LinkedList<ClassFileEntry>();
            alternatives.add(this);
        }
        assert !alternatives.contains(alt) : "alternatives.contains(alt)";
        alternatives.add(alt);
        alt.alternatives = alternatives;
    }
}
