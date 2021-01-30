package de.spricom.dessert.slicing;

import de.spricom.dessert.matching.NamePattern;
import de.spricom.dessert.util.Predicate;
import de.spricom.dessert.util.Predicates;

import java.util.HashSet;
import java.util.Set;

final class DerivedSlice extends AbstractSlice {
    private final NamePattern namePattern;
    private final Predicate<Clazz> predicate;
    private final Set<Clazz> cache = new HashSet<Clazz>();

    DerivedSlice(NamePattern namePattern, Predicate<Clazz> predicate) {
        this.namePattern = namePattern;
        this.predicate = predicate;
    }

    DerivedSlice(NamePattern namePattern) {
        this(namePattern, Predicates.<Clazz>any());
    }

    DerivedSlice(Predicate<Clazz> predicate) {
        this(NamePattern.ANY_NAME, predicate);
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
    public Slice slice(String pattern) {
        return new DerivedSlice(namePattern.and(NamePattern.of(pattern)), predicate);
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
        if (!namePattern.matches(entry.getName())) {
            return false;
        }
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
    public boolean isIterable() {
        return false;
    }

    @Override
    public Set<Clazz> getClazzes() {
        throw new UnsupportedOperationException("Cannot materialize DerivedSlice");
    }

    public String toString() {
        return "slice from " + predicate;
    }
}
