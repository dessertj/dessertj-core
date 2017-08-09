package de.spricom.dessert.resolve;

import java.io.File;
import java.util.Objects;

abstract class ClassContainer {
    private ClassPackage firstChild;
    private ClassFileEntry[] classes;

    public abstract String getPackageName();
    public abstract File getRootFile();

    ClassPackage getFirstChild() {
        return firstChild;
    }
    
    void add(ClassPackage cp) {
        if (firstChild == null) {
            firstChild = cp;
            return;
        }
        ClassPackage previous = firstChild;
        while (previous.getNextSibling() != null) {
            previous = previous.getNextSibling();
        }
        previous.setNextSibling(cp);
    }
    
    boolean isLeaf() {
        return firstChild == ClassPackage.NONE;
    }
    
    void setLeaf() {
        firstChild = ClassPackage.NONE;
    }
    
    ClassFileEntry[] getClasses() {
        return classes;
    }
    
    void setClasses(ClassFileEntry[] classes) {
        this.classes = classes;
    }
    
    public ClassContainer find(String segment) {
        Objects.requireNonNull(firstChild, "firstChild");
        if (firstChild == ClassPackage.NONE) {
            return null;
        }
        ClassPackage p = firstChild;
        while (p != null) {
            if (segment.equals(p.getName())) {
                return p;
            }
            p = p.getNextSibling();
        }
        return null;
    }
}
