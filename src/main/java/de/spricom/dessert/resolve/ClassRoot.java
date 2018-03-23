package de.spricom.dessert.resolve;

import java.io.File;

public abstract class ClassRoot extends ClassPackage {
    private final ClassResolver resolver;
    private final File rootFile;

    protected ClassRoot(ClassResolver resolver, File rootFile) {
        this.resolver = resolver;
        this.rootFile = rootFile;
    }

    protected abstract void scan(ClassCollector classCollector);

    protected final ClassPackage addPackage(String packageName) {
        ClassPackage cp = resolver.getPackage(rootFile, packageName);
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
        while (cp != null && !rootFile.equals(cp.getRootFile())) {
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
    
    protected final void addClass(ClassEntry cf) {
        resolver.addClass(cf);
    }

    @Override
    public final ClassRoot getRoot() {
        return this;
    }

    public final File getRootFile() {
        return rootFile;
    }
}
