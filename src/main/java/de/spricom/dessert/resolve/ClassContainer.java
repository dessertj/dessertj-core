package de.spricom.dessert.resolve;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A class container is either the root package or some named package.
 */
public abstract class ClassContainer<A extends ClassContainer> {
    private final List<ClassPackage> subPackages = new ArrayList<ClassPackage>();
    private List<ClassEntry> classes = new ArrayList<ClassEntry>();
    private List<A> alternatives;

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

    public abstract ClassRoot getRoot();

    /**
     * @return the probably empty list of all direct nested packages of this package
     */
    public List<ClassPackage> getSubPackages() {
        return subPackages;
    }

    /**
     * @return the list of all classes directly contained in this package
     */
    public List<ClassEntry> getClasses() {
        return classes;
    }

    // TODO: Replace by addClass
    public void setClasses(List<ClassEntry> classes) {
        this.classes = classes;
    }

    public List<A> getAlternatives() {
        return alternatives;
    }

    void addAlternative(A alt) {
        if (alternatives == null) {
            alternatives = new LinkedList<A>();
            alternatives.add((A)this);
            alternatives.add(alt);
            ((ClassContainer)alt).alternatives = alternatives;
        }
    }
}
