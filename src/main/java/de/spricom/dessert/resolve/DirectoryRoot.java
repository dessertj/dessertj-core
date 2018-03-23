package de.spricom.dessert.resolve;

import java.io.File;
import java.io.IOException;

final class DirectoryRoot extends ClassRoot {
    public DirectoryRoot(File dir) {
        super(dir);
    }

    @Override
    protected void scan(ClassCollector collector) throws IOException {
        scan(collector, this, getRootFile(), "");
    }

    private void scan(ClassCollector collector, ClassPackage pckg, File dir, String prefix) throws IOException {
        collector.addPackage(pckg);
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                String packageName = prefix + file.getName();
                ClassPackage subPackage = new ClassPackage(pckg, packageName);
                scan(collector, subPackage, file, packageName + ".");
            } else if (file.getName().endsWith(".class")) {
                ClassEntry classEntry = new DirectoryClassEntry(pckg, file);
                pckg.addClass(classEntry);
                collector.addClass(classEntry);
            }
        }
    }
}
