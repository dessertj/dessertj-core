package de.spricom.dessert.resolve;

import java.io.File;
import java.io.IOException;

public abstract class ClassRoot extends ClassContainer {
   private final File file;

    protected ClassRoot(File file) {
        this.file = file;
    }
    
    @Override
    public String getPackageName() {
        return "";
    }

    @Override
    public File getRootFile() {
        return file;
    }

    public abstract boolean resolve(String packagename) throws IOException;
}
