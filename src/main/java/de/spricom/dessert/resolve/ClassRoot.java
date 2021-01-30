package de.spricom.dessert.resolve;

import de.spricom.dessert.matching.NamePattern;

import java.io.File;
import java.io.IOException;

public abstract class ClassRoot extends ClassPackage implements TraversalRoot {
    private final File rootFile;

    protected ClassRoot(File rootFile) {
        this.rootFile = rootFile;
    }

    protected abstract void scan(ClassCollector classCollector) throws IOException;

    public final void traverse(NamePattern pattern, ClassVisitor visitor) {
        traverse(pattern.matcher(), visitor);
    }

    @Override
    public final ClassRoot getRoot() {
        return this;
    }

    public final File getRootFile() {
        return rootFile;
    }

    @Override
    public String toString() {
        return "root " + rootFile.getAbsolutePath();
    }
}
