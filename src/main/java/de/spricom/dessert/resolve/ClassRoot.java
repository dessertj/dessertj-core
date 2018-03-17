package de.spricom.dessert.resolve;

import java.io.File;

public abstract class ClassRoot extends ClassContainer {
    private final ClassResolver resolver;
    private final File file;

    protected ClassRoot(ClassResolver resolver, File file) {
        this.resolver = resolver;
        this.file = file;
    }

    final ClassPackage addPackage(String packageName) {
        ClassPackage cp = resolver.getPackage(file, packageName);
        if (cp != null) {
            return cp;
        }
        int index = packageName.lastIndexOf('.');
        if (index == -1) {
            return addPackage(this, packageName);
        } else {
            String parentName = packageName.substring(0, index);
            return addPackage(addPackage(parentName), packageName);
        }
    }

    final ClassPackage addPackage(ClassContainer parent, String packageName) {
        ClassPackage alt = resolver.getPackage(packageName);
        ClassPackage cp = alt;
        while (cp != null && !file.equals(cp.getRootFile())) {
            alt = cp;
            cp = alt.getNextAlternative();
        }
        if (cp == null) {
            cp = new ClassPackage(this, parent, packageName);
            parent.getSubPackages().add(cp);
            if (alt == null) {
                resolver.addPackage(cp);
            } else {
                alt.setNextAlternative(cp);
            }
        }
        return cp;
    }
    
    final void addClass(ClassFileEntry cf) {
        resolver.addClass(cf);
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
