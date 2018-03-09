package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.ClassContainer;
import de.spricom.dessert.resolve.ClassPackage;
import de.spricom.dessert.resolve.ClassPredicate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A SliceSet is as Set of the slices for which the elements of each
 * {@link Slice} have common properties. I. e. they belong to the same parent
 * package, the same root, implement the same interface, comply with the same
 * naming convention etc.
 */
public final class ManifestSliceSet implements Iterable<Slice>, SliceSet {
    private final Set<Slice> slices;

    ManifestSliceSet() {
        slices = new HashSet<Slice>();
    }

    ManifestSliceSet(int expectedSize) {
        slices = new HashSet<Slice>(expectedSize);
    }

    ManifestSliceSet(Slice slice) {
        slices = Collections.singleton(slice);
    }

    @Override
    public Iterator<Slice> iterator() {
        return slices.iterator();
    }

    @Override
    public SliceSet with(final SliceSet other) {
        if (other instanceof ManifestSliceSet) {
            return with((ManifestSliceSet) other);
        }
        ClassPredicate<SliceEntry> combined = new ClassPredicate<SliceEntry>() {
            @Override
            public boolean test(SliceEntry sliceEntry) {
                return contains(sliceEntry) || other.contains(sliceEntry);
            }
        };
        return new LazySliceSet(combined);
    }

    public ManifestSliceSet with(final ManifestSliceSet other) {
        ManifestSliceSet ss = new ManifestSliceSet(slices.size() + other.slices.size());
        ss.slices.addAll(slices);
        ss.slices.addAll(other.slices);
        return ss;
    }

    public ManifestSliceSet without(final SliceSet other) {
        ClassPredicate<SliceEntry> excluded = new ClassPredicate<SliceEntry>() {
            @Override
            public boolean test(SliceEntry sliceEntry) {
                return !other.contains(sliceEntry);
            }
        };
        return (ManifestSliceSet) slice(excluded);
    }

    @Override
    public ManifestSliceSet slice(ClassPredicate<SliceEntry> predicate) {
        ManifestSliceSet ss = new ManifestSliceSet(slices.size());
        for (Slice s : slices) {
            Slice filtered = s.slice(predicate);
            if (!filtered.getEntries().isEmpty()) {
                ss.slices.add(filtered);
            }
        }
        return ss;
    }

    @Override
    public boolean contains(SliceEntry entry) {
        for (Slice s : slices) {
            if (s.getEntries().contains(entry)) {
                return true;
            }
        }
        return false;
    }

    void add(ClassContainer cc, SliceContext context) {
        if (cc == null || cc.getClasses() == null || cc.getClasses().isEmpty()) {
            return;
        }
        slices.add(new Slice(cc, context));
    }

    void addRecursive(ClassContainer cc, SliceContext context) {
        add(cc, context);
        for (ClassPackage subp : cc.getSubPackages()) {
            addRecursive(subp, context);
        }
    }
}
