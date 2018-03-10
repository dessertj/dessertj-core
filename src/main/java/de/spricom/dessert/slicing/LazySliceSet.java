package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;

import java.util.HashSet;
import java.util.Set;

final class LazySliceSet implements SliceSet {
    private final Predicate<SliceEntry> predicate;
    private final Set<SliceEntry> cache = new HashSet<SliceEntry>();

    LazySliceSet(Predicate<SliceEntry> predicate) {
        this.predicate = predicate;
    }

    @Override
    public SliceSet with(final SliceSet other) {
        return new LazySliceSet(new Predicate<SliceEntry>() {
            @Override
            public boolean test(SliceEntry sliceEntry) {
                return contains(sliceEntry) || other.contains(sliceEntry);
            }
        });
    }

    @Override
    public SliceSet without(final SliceSet other) {
        return new LazySliceSet(new Predicate<SliceEntry>() {
            @Override
            public boolean test(SliceEntry sliceEntry) {
                return contains(sliceEntry) && !other.contains(sliceEntry);
            }
        });
    }

    @Override
    public SliceSet slice(final Predicate<SliceEntry> predicate) {
        return new LazySliceSet(new Predicate<SliceEntry>() {
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
