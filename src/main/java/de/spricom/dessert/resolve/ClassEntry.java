package de.spricom.dessert.resolve;

import de.spricom.dessert.classfile.ClassFile;

import java.util.LinkedList;
import java.util.List;

public abstract class ClassEntry {
    private final String classname;
    private final ClassPackage pckg;
    private List<ClassEntry> alternatives;
    private ClassFile classFile;

    protected ClassEntry(String classname, ClassPackage pckg) {
        this.classname = classname;
        this.pckg = pckg;
    }

    public String getClassname() {
        return classname;
    }

    public ClassFile getClassfile() {
        if (classFile == null) {
            classFile = resolveClassFile();
            assert classname.equals(classFile.getThisClass()) : classname + " != " + classFile.getThisClass();
        }
        return classFile;
    }

    protected abstract ClassFile resolveClassFile();

    public ClassPackage getPackage() {
        return pckg;
    }

    public List<ClassEntry> getAlternatives() {
        return alternatives;
    }

    void addAlternative(ClassEntry alt) {
        assert alt.alternatives == null : "alt.alternatives != null";
        if (alternatives == null) {
            alternatives = new LinkedList<ClassEntry>();
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
