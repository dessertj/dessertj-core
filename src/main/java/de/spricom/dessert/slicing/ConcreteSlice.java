package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;
import de.spricom.dessert.util.SetHelper;

import java.util.HashSet;
import java.util.Set;

/**
 * A concrete slice is a concrete collection of classes.
 * Hence it contains a set of {@link Clazz}.
 * The sum or difference on concrete slices
 * produce a concrete slice again.
 */
public class ConcreteSlice extends AbstractSlice {
    private final Set<Clazz> entries;

    protected ConcreteSlice(Set<Clazz> entries) {
        this.entries = entries;
    }

    @Override
    public Slice combine(final Slice other) {
        if (other instanceof ConcreteSlice) {
            ConcreteSlice slice = new ConcreteSlice(SetHelper.union(entries, other.getSliceEntries()));
            return slice;
        }
        Predicate<Clazz> combined = new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz sliceEntry) {
                return contains(sliceEntry) || other.contains(sliceEntry);
            }
        };
        return new DerivedSlice(combined);
    }

    public ConcreteSlice without(final Slice other) {
        Predicate<Clazz> excluded = new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz sliceEntry) {
                return !other.contains(sliceEntry);
            }
        };
        return (ConcreteSlice) slice(excluded);
    }

    @Override
    public ConcreteSlice slice(Predicate<Clazz> predicate) {
        Set<Clazz> filtered = new HashSet<Clazz>();
        for (Clazz entry : entries) {
            if (predicate.test(entry)) {
                filtered.add(entry);
            }
        }
        return new ConcreteSlice(filtered);
    }

    @Override
    public boolean contains(Clazz entry) {
        return entries.contains(entry);
    }

    @Override
    public boolean canResolveSliceEntries() {
        return true;
    }

    public Set<Clazz> getSliceEntries() {
        return entries;
    }

    public String toString() {
        return entries.toString();
    }
}
