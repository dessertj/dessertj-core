package de.spricom.dessert.resolve;

import java.io.File;
import java.util.Objects;

abstract class ClassContainer {
    static final ClassPackage NONE = new ClassPackage(null, null);

    private ClassPackage firstChild;
    private ClassFileEntry[] classes;

    public abstract String getPackageName();
    public abstract File getRootFile();

    ClassPackage getFirstChild() {
        return firstChild;
    }
    
    void setFirstChild(ClassPackage firstChild) {
        this.firstChild = firstChild;
    }
    
    ClassFileEntry[] getClasses() {
        return classes;
    }
    
    void setClasses(ClassFileEntry[] classes) {
        this.classes = classes;
    }
    
    public ClassContainer find(String segment) {
        Objects.requireNonNull(firstChild, "firstChild");
        if (firstChild == NONE) {
            return null;
        }
        ClassPackage p = firstChild;
        while (p != null) {
            if (segment.equals(p.getName())) {
                return p;
            }
        }
        return null;
    }
}
