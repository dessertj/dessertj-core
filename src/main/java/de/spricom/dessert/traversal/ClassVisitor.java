package de.spricom.dessert.traversal;

import java.io.File;
import java.io.InputStream;

public interface ClassVisitor {
    void visit(File root, String classname, InputStream content);
}
