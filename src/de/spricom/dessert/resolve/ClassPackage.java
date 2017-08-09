package de.spricom.dessert.resolve;

import java.io.File;

public class ClassPackage extends ClassContainer {
    static final ClassPackage NONE = new ClassPackage();

    private final ClassContainer parent;
    private final String name;
    private ClassPackage nextSibling;

    private ClassPackage() {
        parent = null;
        name = "NONE";
    }
    
    public ClassPackage(ClassContainer parent, String name) {
        this.parent = parent;
        this.name = name;
        parent.add(this);
    }

    @Override
    public String getPackageName() {
        return (parent.getPackageName() + "." + name).substring(1);
    }

    @Override
    public File getRootFile() {
        return parent.getRootFile();
    }

    public ClassPackage getNextSibling() {
        return nextSibling;
    }

    public void setNextSibling(ClassPackage nextSibling) {
        this.nextSibling = nextSibling;
    }

    public ClassContainer getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }
}
