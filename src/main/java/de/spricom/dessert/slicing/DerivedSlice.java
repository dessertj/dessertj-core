package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;

import java.util.HashSet;
import java.util.Set;

final class DerivedSlice extends AbstractSlice {
    private final Predicate<SliceEntry> predicate;
    private final Set<SliceEntry> cache = new HashSet<SliceEntry>();

    DerivedSlice(Predicate<SliceEntry> predicate) {
        this.predicate = predicate;
    }

    @Override
    public Slice with(final Slice other) {
        return new DerivedSlice(new Predicate<SliceEntry>() {
            @Override
            public boolean test(SliceEntry sliceEntry) {
                return contains(sliceEntry) || other.contains(sliceEntry);
            }
        });
    }

    @Override
    public Slice without(final Slice other) {
        return new DerivedSlice(new Predicate<SliceEntry>() {
            @Override
            public boolean test(SliceEntry sliceEntry) {
                return contains(sliceEntry) && !other.contains(sliceEntry);
            }
        });
    }

    @Override
    public Slice slice(final Predicate<SliceEntry> predicate) {
        return new DerivedSlice(new Predicate<SliceEntry>() {
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

    @Override
    public boolean canResolveSliceEntries() {
        return false;
    }

    @Override
    public Set<SliceEntry> getSliceEntries() {
        throw new IllegalStateException("Cannot materialize DerivedSlice");
    }
}
