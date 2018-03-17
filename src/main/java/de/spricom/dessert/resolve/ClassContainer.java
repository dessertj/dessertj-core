package de.spricom.dessert.resolve;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * A class container is either the root package or some named package.
 */
public abstract class ClassContainer {
    private final LinkedList<ClassPackage> subPackages = new LinkedList<ClassPackage>();
    private List<ClassFileEntry> classes;

    /**
     * @return the package name of this container, an empty string if the package is the root package
     */
    public abstract String getPackageName();

    /**
     * The root file is the directory or jar file enlisted on the class-path that contains the package
     * represented by this class.
     * 
     * @return the jar file or the classes root directory
     */
    public abstract File getRootFile();

    /**
     * @return the probably empty list of all direct nested packages of this package
     */
    public List<ClassPackage> getSubPackages() {
        return subPackages;
    }

    /**
     * @return the list of all classes directly contained in this package
     */
    public List<ClassFileEntry> getClasses() {
        return classes;
    }

    void setClasses(List<ClassFileEntry> classes) {
        this.classes = classes;
    }
}
