package de.spricom.dessert.resolve;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public final class ClassPackage extends ClassContainer {
    private final ClassRoot root;
    private final String packageName;
    private final ClassContainer parent;
    private ClassPackage nextAlternative;
    private List<ClassPackage> alternatives;

    public ClassPackage(ClassRoot root, ClassContainer parent, String packageName) {
        this.root = root;
        this.parent = parent;
        this.packageName = packageName;
    }

    public ClassContainer getParent() {
        return parent;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public File getRootFile() {
        return root.getRootFile();
    }

    @Override
    public ClassRoot getRoot() {
        return null;
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
