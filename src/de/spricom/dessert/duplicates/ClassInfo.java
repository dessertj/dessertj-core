package de.spricom.dessert.duplicates;

import java.io.File;

public class ClassInfo {
    private final File root;
    private final String classname;
    private final byte[] hash;

    public ClassInfo(File root, String classname, byte[] hash) {
        super();
        this.root = root;
        this.classname = classname;
        this.hash = hash;
    }

    public File getRoot() {
        return root;
    }

    public String getClassname() {
        return classname;
    }

    public byte[] getHash() {
        return hash;
    }
}