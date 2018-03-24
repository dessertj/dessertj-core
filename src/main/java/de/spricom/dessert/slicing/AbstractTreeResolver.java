package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.ClassEntry;
import de.spricom.dessert.resolve.ClassPackage;

import java.util.*;

public abstract class AbstractTreeResolver implements EntryResolver {
    private final SliceContext sc;
    private Set<SliceEntry> sliceEntries;

    public AbstractTreeResolver(SliceContext sc) {
        this.sc = sc;
    }

    @Override
    public final Set<SliceEntry> getSliceEntries() {
        if (sliceEntries == null) {
            sliceEntries = new HashSet<SliceEntry>();
            resolve();
        }
        return sliceEntries;
    }

    protected abstract void resolve();

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

    protected final void add(ClassEntry ce) {
        sliceEntries.add(sc.getSliceEntry(ce));
    }
}
