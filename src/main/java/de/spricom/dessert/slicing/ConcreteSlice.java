package de.spricom.dessert.slicing;

import de.spricom.dessert.groups.PackageSlice;
import de.spricom.dessert.util.Predicate;
import de.spricom.dessert.util.SetHelper;

import java.util.HashSet;
import java.util.Set;

/**
 * A concrete slice is a concrete collection of classes.
 * Hence it contains a set of {@link SliceEntry}.
 * The sum or difference on concrete slices
 * produce a concrete slice again.
 */
public class ConcreteSlice extends AbstractSlice {
    private final Set<SliceEntry> entries;

    protected ConcreteSlice(Set<SliceEntry> entries) {
        this.entries = entries;
    }

    @Override
    public Slice with(final Slice other) {
        if (other instanceof ConcreteSlice) {
            ConcreteSlice slice = new ConcreteSlice(SetHelper.unite(entries, other.getSliceEntries()));
            return slice;
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

    @Override
    public boolean canResolveSliceEntries() {
        return true;
    }

    public Set<SliceEntry> getSliceEntries() {
        return entries;
    }

    public String toString() {
        return entries.toString();
    }
}
