package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.ClassContainer;
import de.spricom.dessert.resolve.ClassPackage;
import de.spricom.dessert.util.Predicate;

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
public final class ConcreteSlice implements Iterable<PackageSlice>, Slice {
    private final Set<PackageSlice> packageSlices;

    ConcreteSlice() {
        packageSlices = new HashSet<PackageSlice>();
    }

    ConcreteSlice(int expectedSize) {
        packageSlices = new HashSet<PackageSlice>(expectedSize);
    }

    ConcreteSlice(PackageSlice packageSlice) {
        packageSlices = Collections.singleton(packageSlice);
    }

    @Override
    public Iterator<PackageSlice> iterator() {
        return packageSlices.iterator();
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
        ConcreteSlice ss = new ConcreteSlice(packageSlices.size() + other.packageSlices.size());
        ss.packageSlices.addAll(packageSlices);
        ss.packageSlices.addAll(other.packageSlices);
        return ss;
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
        ConcreteSlice ss = new ConcreteSlice(packageSlices.size());
        for (PackageSlice s : packageSlices) {
            PackageSlice filtered = s.slice(predicate);
            if (!filtered.getEntries().isEmpty()) {
                ss.packageSlices.add(filtered);
            }
        }
        return ss;
    }

    @Override
    public boolean contains(SliceEntry entry) {
        for (PackageSlice s : packageSlices) {
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
        packageSlices.add(new PackageSlice(cc, context));
    }

    void addRecursive(ClassContainer cc, SliceContext context) {
        add(cc, context);
        for (ClassPackage subp : cc.getSubPackages()) {
            addRecursive(subp, context);
        }
    }
}
