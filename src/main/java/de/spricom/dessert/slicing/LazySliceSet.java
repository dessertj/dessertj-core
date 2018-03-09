package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.ClassPredicate;

import java.util.HashSet;
import java.util.Set;

final class LazySliceSet implements SliceSet {
    private final ClassPredicate<SliceEntry> predicate;
    private final Set<SliceEntry> cache = new HashSet<SliceEntry>();

    LazySliceSet(ClassPredicate<SliceEntry> predicate) {
        this.predicate = predicate;
    }

    @Override
    public SliceSet with(final SliceSet other) {
        return new LazySliceSet(new ClassPredicate<SliceEntry>() {
            @Override
            public boolean test(SliceEntry sliceEntry) {
                return contains(sliceEntry) || other.contains(sliceEntry);
            }
        });
    }

    @Override
    public SliceSet without(final SliceSet other) {
        return new LazySliceSet(new ClassPredicate<SliceEntry>() {
            @Override
            public boolean test(SliceEntry sliceEntry) {
                return contains(sliceEntry) && !other.contains(sliceEntry);
            }
        });
    }

    @Override
    public SliceSet slice(final ClassPredicate<SliceEntry> predicate) {
        return new LazySliceSet(new ClassPredicate<SliceEntry>() {
            @Override
            public boolean test(SliceEntry sliceEntry) {
                return contains(sliceEntry) && predicate.test(sliceEntry);
            }
        });
    }

    @Override
    public boolean contains(SliceEntry entry) {
        if (cache.contains(entry)) {
            return true;
        }
        boolean member = predicate.test(entry);
        if (member) {
            cache.add(entry);
        }
        return member;
    }
}
