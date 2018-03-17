package de.spricom.dessert.resolve;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

final class DirectoryRoot extends ClassRoot {
    public DirectoryRoot(ClassResolver resolver, File file) throws IOException {
        super(resolver, file);
        scan(this, getRootFile(), "");
    }

    private void scan(ClassContainer cc, File dir, String prefix) throws IOException {
        assert cc.getClasses() == null : "Classes for " + cc + " already scanned.";
        List<ClassFileEntry> classes = new LinkedList<ClassFileEntry>();
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                String packageName = prefix + file.getName();
                scan(addPackage(cc, packageName), file, packageName + ".");
            } else if (file.getName().endsWith(".class")) {
                classes.add(scanClass(cc, file));
            }
        }
        cc.setClasses(classes);
    }

    private ClassFileEntry scanClass(ClassContainer cc, File file) throws IOException {
        ClassFileEntry entry = new DirectoryClassFileEntry(cc, file);
        addClass(entry);
        return entry;
    }
}
