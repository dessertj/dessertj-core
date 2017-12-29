package de.spricom.dessert.duplicates;

import java.io.File;

public class DuplicateClassException extends RuntimeException {
    private static final long serialVersionUID = 3412954876231434318L;

    private final String classname;
    private final File root1, root2;

    public DuplicateClassException(String classname, File root1, File root2) {
        super("There is " + classname + " in " + root1.getAbsolutePath() + " and " + root2.getAbsolutePath() + ".");
        this.classname = classname;
        this.root1 = root1;
        this.root2 = root2;
    }

    public String getClassname() {
        return classname;
    }

    public File getRoot1() {
        return root1;
    }

    public File getRoot2() {
        return root2;
    }
}
