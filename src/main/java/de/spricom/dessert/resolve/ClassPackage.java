package de.spricom.dessert.resolve;

import java.io.File;

public final class ClassPackage extends ClassContainer {
    private final ClassRoot root;
    private final String packageName;
    private final ClassContainer parent;
    private ClassPackage nextAlternative;

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

    public ClassPackage getNextAlternative() {
        return nextAlternative;
    }

    void setNextAlternative(ClassPackage nextAlternative) {
        this.nextAlternative = nextAlternative;
    }

    @Override
    public final String toString() {
        return packageName;
    }
}
