package de.spricom.dessert.resolve;

import java.io.File;

public class ClassPackage extends ClassContainer {
    private final ClassRoot root;
    private final String packageName;
    private final ClassContainer parent;
 
    public ClassPackage(ClassRoot root, String name) {
        this.root = root;
        this.packageName = name;
        root.packages.put(name, this);

        int index = packageName.lastIndexOf('.');
        if (index == -1) {
            parent = root;
        } else {
            String parentName = packageName.substring(0, index);
            ClassPackage pp = root.packages.get(parentName);
            if (pp != null) {
                parent = pp;
            } else {
                parent = new ClassPackage(root, parentName);
            }
        }
        
        parent.getSubPackages().add(this);
    }

    public ClassContainer getParent() {
        return parent;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public File getRootFile() {
        return root.getRootFile();
    }
    
    @Override
    public final String toString() {
        return packageName;
    }
}
