package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;

import java.util.HashSet;
import java.util.Set;

final class DerivedSlice extends AbstractSlice {
    private final Predicate<Clazz> predicate;
    private final Set<Clazz> cache = new HashSet<Clazz>();

    DerivedSlice(Predicate<Clazz> predicate) {
        this.predicate = predicate;
    }

    @Override
    public Slice combine(final Slice other) {
        return new DerivedSlice(new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz clazz) {
                return contains(clazz) || other.contains(clazz);
            }
        });
    }

    @Override
    public Slice slice(final Predicate<Clazz> predicate) {
        return new DerivedSlice(new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz clazz) {
                return contains(clazz) && predicate.test(clazz);
            }
        });
    }

    @Override
    public boolean contains(Clazz entry) {
        if (cache.contains(entry)) {
            return true;
        }
        boolean member = predicate.test(entry);
        if (member) {
            cache.add(entry);
        }
        return member;
    }

    @Override
    public boolean canResolveSliceEntries() {
        return false;
    }

    @Override
    public Set<Clazz> getSliceEntries() {
        throw new IllegalStateException("Cannot materialize DerivedSlice");
    }

    public String toString() {
        return "slice from " + predicate;
    }
}
