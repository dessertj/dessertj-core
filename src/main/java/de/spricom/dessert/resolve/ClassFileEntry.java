package de.spricom.dessert.resolve;

import de.spricom.dessert.classfile.ClassFile;

import java.util.LinkedList;
import java.util.List;

public abstract class ClassFileEntry {
    private final ClassContainer pckg;
    private List<ClassFileEntry> alternatives;
    private ClassFile classFile;

    protected ClassFileEntry(ClassContainer pckg) {
        this.pckg = pckg;
    }

    public ClassFile getClassfile() {
        if (classFile == null) {
            classFile = resolveClassFile();
        }
        return classFile;
    }

    protected abstract ClassFile resolveClassFile();

    public ClassContainer getPackage() {
        return pckg;
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

    public String getFilename() {
        return pckg.getRootFile().getName();
    }
}
