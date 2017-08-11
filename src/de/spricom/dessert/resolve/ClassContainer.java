package de.spricom.dessert.resolve;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public abstract class ClassContainer {
    private final LinkedList<ClassPackage> subPackages = new LinkedList<>();
    private ClassFileEntry[] classes;

    public abstract String getPackageName();
    public abstract File getRootFile();

    
    public List<ClassPackage> getSubPackages() {
        return subPackages;
    }
    
    ClassFileEntry[] getClasses() {
        return classes;
    }
    
    void setClasses(ClassFileEntry[] classes) {
        this.classes = classes;
    }
    
}
