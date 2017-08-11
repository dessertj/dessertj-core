package de.spricom.dessert.resolve;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class ClassRoot extends ClassContainer {
    private final File file;
    final Map<String, ClassPackage> packages = new HashMap<>();

    protected ClassRoot(File file) {
        this.file = file;
    }

    @Override
    public final String getPackageName() {
        return "";
    }

    @Override
    public final File getRootFile() {
        return file;
    }
}
