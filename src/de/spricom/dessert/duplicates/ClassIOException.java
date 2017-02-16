package de.spricom.dessert.duplicates;

import java.io.File;

public class ClassIOException extends RuntimeException {
    private static final long serialVersionUID = -2706715212531169540L;

    private final String classname;
    private final File root;

    public ClassIOException(String classname, File root, Exception cause) {
        super("Cannot read conent of  " + classname + " in " + root.getAbsolutePath(), cause);
        this.classname = classname;
        this.root = root;
    }

    public String getClassname() {
        return classname;
    }

    public File getRoot() {
        return root;
    }
}
