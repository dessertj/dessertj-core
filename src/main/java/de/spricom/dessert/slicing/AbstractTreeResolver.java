package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.ClassEntry;
import de.spricom.dessert.resolve.ClassPackage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractTreeResolver extends AbstractClazzResolver {
    private final Classpath cp;

    public AbstractTreeResolver(Classpath cp) {
        super(cp);
        this.cp = cp;
    }

    protected final void addRecursiveWithAlternatives(ClassPackage cp) {
        Collection<ClassPackage> alternatives = cp.getAlternatives() == null ? Collections.singleton(cp) : cp.getAlternatives();
        for (ClassPackage alt : alternatives) {
            add(alt);
            List<ClassPackage> subPackages = alt.getSubPackages();
            for (ClassPackage subp : subPackages) {
                addRecursiveWithAlternatives(subp);
            }
        }
    }

    protected void addRecursive(ClassPackage cp) {
        add(cp);
        List<ClassPackage> subPackages = cp.getSubPackages();
        for (ClassPackage subp : subPackages) {
            addRecursive(subp);
        }
    }

    protected final void add(ClassPackage cp) {
        List<ClassEntry> classes = cp.getClasses();
        for (ClassEntry ce : classes) {
            add(ce);
        }
    }

    protected final void addWithAlternatives(ClassPackage cp) {
        Collection<ClassPackage> alternatives = cp.getAlternatives() == null ? Collections.singleton(cp) : cp.getAlternatives();
        for (ClassPackage alt : alternatives) {
            add(alt);
        }
    }
}
