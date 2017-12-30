package de.spricom.dessert.resolve;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public abstract class ClassContainer {
    private final LinkedList<ClassPackage> subPackages = new LinkedList<ClassPackage>();
    private List<ClassFileEntry> classes;

    public abstract String getPackageName();

    public abstract File getRootFile();

    public List<ClassPackage> getSubPackages() {
        return subPackages;
    }

    public List<ClassFileEntry> getClasses() {
        return classes;
    }

    void setClasses(List<ClassFileEntry> classes) {
        this.classes = classes;
    }
}
