package de.spricom.dessert.resolve;

import java.io.File;

public class DirectoryRoot extends ClassRoot {
    public DirectoryRoot(File file) {
        super(file);
    }

    @Override
    public boolean resolve(String packagename) {
        if (getFirstChild() != null) {
            return true;
        }
        File packageDir = new File(getRootFile(), packagename.replace('.', '/'));
        if (!(packageDir.exists() && packageDir.isDirectory())) {
            return false;
        }
        scan(this, getRootFile());
        return true;
    }
    
    private void scan(ClassContainer cc, File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scan(new ClassPackage(cc, file.getName()), file);
            }
        }
        if (cc.getFirstChild() == null) {
            cc.setLeaf();
        }
    }
}
