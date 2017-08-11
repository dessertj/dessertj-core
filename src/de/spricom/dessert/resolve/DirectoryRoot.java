package de.spricom.dessert.resolve;

import java.io.File;

public class DirectoryRoot extends ClassRoot {
    public DirectoryRoot(File file) {
        super(file);
        scan(this, getRootFile(), "");
    }

    private void scan(ClassContainer cc, File dir, String prefix) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                String packageName = prefix + file.getName();
                scan(new ClassPackage(this, packageName), file, packageName + ".");
            }
        }
    }
}
