package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;
import de.spricom.dessert.util.SetHelper;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A concrete slice is a concrete collection of classes.
 * Hence it contains a set of {@link Clazz}.
 * The sum or difference on concrete slices
 * produce a concrete slice again.
 */
public class ConcreteSlice extends AbstractSlice implements Concrete {
    private final Set<Clazz> entries;

    protected ConcreteSlice(Set<Clazz> entries) {
        this.entries = entries;
    }

    @Override
    public Slice combine(final Slice other) {
        if (other instanceof Concrete) {
            ConcreteSlice slice = new ConcreteSlice(SetHelper.union(entries, other.getSliceEntries()));
            return slice;
        }
        Predicate<Clazz> combined = new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz sliceEntry) {
                return contains(sliceEntry) || other.contains(sliceEntry);
            }
        };
        DerivedSlice derived = new DerivedSlice(combined);
        if (other.canResolveSliceEntries()) {
            EntryResolver resolver = new EntryResolver() {
                @Override
                public Set<Clazz> getSliceEntries() {
                    return SetHelper.union(entries, other.getSliceEntries());
                }
            };
            return new DeferredSlice(derived, resolver);
        }
        return derived;
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
        StringBuilder sb = new StringBuilder("slice of [");
        Iterator<Clazz> iter = entries.iterator();
        boolean first = true;
        while (iter.hasNext() && sb.length() < 60) {
            Clazz entry = iter.next();
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(entry.getName());
        }
        if (iter.hasNext()) {
            sb.append(" ...");
        }
        sb.append("]");
        return sb.toString();
    }

}
