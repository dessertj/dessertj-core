package de.spricom.dessert.resolve;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClassPackage {
    private final String packageName;
    private final ClassPackage parent;
    private final List<ClassPackage> subPackages = new ArrayList<ClassPackage>();
    private final List<ClassEntry> classes = new ArrayList<ClassEntry>();
    private List<ClassPackage> alternatives;

    protected ClassPackage() {
        packageName = "";
        parent = null;
    }

    public ClassPackage(ClassPackage parent, String packageName) {
        this.parent = parent;
        this.packageName = packageName;
        assert getSubPackage(packageName) == null : "Package " + packageName + " added twice.";
        parent.subPackages.add(this);
    }

    public ClassPackage getParent() {
        return parent;
    }

    public final String getPackageName() {
        return packageName;
    }

    public ClassRoot getRoot() {
        return parent.getRoot();
    }

    public File getRootFile() {
        return getRoot().getRootFile();
    }

    @Override
    public final String toString() {
        return packageName;
    }

    /**
     * @return the probably empty list of all direct nested packages of this package
     */
    public List<ClassPackage> getSubPackages() {
        return subPackages;
    }

    public ClassPackage getSubPackage(String packageName) {
        for (ClassPackage subPackage : subPackages) {
            if (packageName.equals(subPackage.getPackageName())) {
                return subPackage;
            }
        }
        return null;
    }

    /**
     * @return the list of all classes directly contained in this package
     */
    public List<ClassEntry> getClasses() {
        return classes;
    }

    public void addClass(ClassEntry ce) {
        assert getClass(ce.getClassname()) == null : "Class " + ce.getClassname() + " added twice.";
        classes.add(ce);
    }

    public ClassEntry getClass(String classname) {
        for (ClassEntry ce : classes) {
            if (classname.equals(ce.getClassname())) {
                return ce;
            }
        }
        return null;
    }

    public List<ClassPackage> getAlternatives() {
        return alternatives;
    }

    void addAlternative(ClassPackage alt) {
        assert alt.alternatives == null : "alt.alternatives != null";
        if (alternatives == null) {
            alternatives = new LinkedList<ClassPackage>();
            alternatives.add(this);
        }
        assert !alternatives.contains(alt) : "alternatives.contains(alt)";
        alternatives.add(alt);
        alt.alternatives = alternatives;
    }
}
