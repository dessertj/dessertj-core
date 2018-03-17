package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.ClassContainer;
import de.spricom.dessert.resolve.ClassFileEntry;
import de.spricom.dessert.resolve.ClassPackage;
import de.spricom.dessert.util.Predicate;
import de.spricom.dessert.util.SetHelper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A Slice is as Set of the packageSlices for which the elements of each
 * {@link PackageSlice} have common properties. I. e. they belong to the same parent
 * package, the same root, implement the same interface, comply with the same
 * naming convention etc.
 */
public class ConcreteSlice implements Slice {
    private final Set<SliceEntry> entries;

    protected ConcreteSlice(Set<SliceEntry> entries) {
        this.entries = entries;
    }

    ConcreteSlice() {
        entries = new HashSet<SliceEntry>();
    }

    @Override
    public Slice with(final Slice other) {
        if (other instanceof ConcreteSlice) {
            return with((ConcreteSlice) other);
        }
        Predicate<SliceEntry> combined = new Predicate<SliceEntry>() {
            @Override
            public boolean test(SliceEntry sliceEntry) {
                return contains(sliceEntry) || other.contains(sliceEntry);
            }
        };
        return new DerivedSlice(combined);
    }

    public ConcreteSlice with(final ConcreteSlice other) {
        ConcreteSlice slice = new ConcreteSlice(SetHelper.unite(entries, other.getSliceEntries()));
        return slice;
    }

    public ConcreteSlice without(final Slice other) {
        Predicate<SliceEntry> excluded = new Predicate<SliceEntry>() {
            @Override
            public boolean test(SliceEntry sliceEntry) {
                return !other.contains(sliceEntry);
            }
        };
        return (ConcreteSlice) slice(excluded);
    }

    @Override
    public ConcreteSlice slice(Predicate<SliceEntry> predicate) {
        Set<SliceEntry> filtered = new HashSet<SliceEntry>();
        for (SliceEntry entry : entries) {
            if (predicate.test(entry)) {
                filtered.add(entry);
            }
        }
        return new ConcreteSlice(filtered);
    }

    @Override
    public boolean contains(SliceEntry entry) {
        return entries.contains(entry);
    }

    public Set<SliceEntry> getSliceEntries() {
        return entries;
    }

    public String toString() {
        return entries.toString();
    }

    void add(ClassContainer cc, SliceContext context) {
        if (cc == null || cc.getClasses() == null || cc.getClasses().isEmpty()) {
            return;
        }
        for (ClassFileEntry cf : cc.getClasses()) {
            entries.add(new SliceEntry(context, cf));
        }
    }

    void addRecursive(ClassContainer cc, SliceContext context) {
        add(cc, context);
        for (ClassPackage subp : cc.getSubPackages()) {
            addRecursive(subp, context);
        }
    }
}
