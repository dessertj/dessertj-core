package de.spricom.dessert.resolve;

import java.io.File;
import java.io.IOException;

public abstract class ClassRoot extends ClassPackage {
    private final File rootFile;

    protected ClassRoot(File rootFile) {
        this.rootFile = rootFile;
    }

    protected abstract void scan(ClassCollector classCollector) throws IOException;

    @Override
    public final ClassRoot getRoot() {
        return this;
    }

    public final File getRootFile() {
        return rootFile;
    }
}
