package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.ClassRoot;

import java.net.URI;

/**
 * A special {@link Slice} that represents a whole JAR file, classes directory, module or other
 * single source of classes. The slice contains all its .class files.
 */
public class Root extends AbstractRootSlice {
    private final ClassRoot root;
    private final Classpath classpath;

    Root(ClassRoot root, Classpath classpath) {
        super(root);
        this.root = root;
        this.classpath = classpath;
    }

    public URI getURI() {
        return root.getRootFile().toURI();
    }

    @Override
    Classpath getClasspath() {
        return classpath;
    }

    public String toString() {
        return "root of " + root.getRootFile().getName();
    }
}
