package de.spricom.dessert.resolve;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ClassPackage extends ClassContainer {
    private final String packageName;
    private final ClassPackage parent;
    private ClassPackage nextAlternative;
    private List<ClassPackage> alternatives;

    protected ClassPackage() {
        packageName = "";
        parent = null;
    }

    public ClassPackage(ClassPackage parent, String packageName) {
        this.parent = parent;
        this.packageName = packageName;
    }

    public ClassPackage(ClassRoot root, ClassContainer parent, String packageName) {
        this((ClassPackage)parent, packageName);
    }

    public ClassPackage getParent() {
        return parent;
    }

    public final String getPackageName() {
        return packageName;
    }

    @Override
    public ClassRoot getRoot() {
        return parent.getRoot();
    }

    public File getRootFile() {
        return getRoot().getRootFile();
    }

    public ClassPackage getNextAlternative() {
        return nextAlternative;
    }

    void setNextAlternative(ClassPackage nextAlternative) {
        this.nextAlternative = nextAlternative;
        if (alternatives == null && nextAlternative.alternatives == null) {
            alternatives = new LinkedList<ClassPackage>();
            alternatives.add(this);
            alternatives.add(nextAlternative);
            nextAlternative.alternatives = alternatives;
        } else {
            assert alternatives != null : "alternatives != null violated";
            assert nextAlternative.alternatives == null || alternatives == nextAlternative.alternatives : "alternatives == nextAlternative.alternatives violated";
            alternatives.add(nextAlternative);
            nextAlternative.alternatives = alternatives;
        }
    }

    @Override
    public final String toString() {
        return packageName;
    }

    public List<ClassPackage> getAlternatives() {
        return alternatives;
    }
}
