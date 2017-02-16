package de.spricom.dessert.traversal;

import java.io.File;
import java.io.InputStream;

@FunctionalInterface
public interface ClassVisitor {

    void visit(File root, String classname, InputStream content);
}
