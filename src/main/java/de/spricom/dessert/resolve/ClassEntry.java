package de.spricom.dessert.resolve;

import de.spricom.dessert.classfile.ClassFile;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

/**
 * A ClassEntry represents a single class file within one classes directory or .jar file.
 * It's linked to the {@link ClassPackage} it belongs to.
 * If there is an other .class file with the same name in some other classes directory or
 * .jar file within the {@link ClassResolver} scope it has a reference to a list of all
 * such .class files.
 */
public abstract class ClassEntry {
    private final String classname;
    private final ClassPackage pckg;
    private List<ClassEntry> alternatives;
    private ClassFile classFile;

    protected ClassEntry(String classname, ClassPackage pckg) {
        this.classname = classname;
        this.pckg = pckg;
    }

    /**
     * @return the full qualified classname
     */
    public String getClassname() {
        return classname;
    }

    /**
     * Resolves the {@link ClassFile} by analyzing the byte code. This is a time consuming operation and
     * should be called as late as possible. For .jar files the most time is spent by decompressing the
     * .class file. The result will be cached for further calls.
     *
     * @return a chached or frechly resolved instance of the {@link ClassFile}
     */
    public ClassFile getClassfile() {
        if (classFile == null) {
            classFile = resolveClassFile();
            assert classname.equals(classFile.getThisClass()) : classname + " != " + classFile.getThisClass();
        }
        return classFile;
    }

    protected abstract ClassFile resolveClassFile();

    /**
     * @return the {@link ClassPackage} this ClassEntry belongs to.
     */
    public ClassPackage getPackage() {
        return pckg;
    }

    /**
     * Returns a list of all other .class files with the same fully qualified name.
     *
     * @return the other .class files or null if there are none
     */
    public List<ClassEntry> getAlternatives() {
        return alternatives;
    }

    void addAlternative(ClassEntry alt) {
        assert alt.alternatives == null : "alt.alternatives != null";
        if (alternatives == null) {
            alternatives = new LinkedList<ClassEntry>();
            alternatives.add(this);
        }
        assert !alternatives.contains(alt) : "alternatives.contains(alt)";
        alternatives.add(alt);
        alt.alternatives = alternatives;
    }

    /**
     * @return an {@link URI} which identifies the .class file uniquely
     */
    public abstract URI getURI();
}
