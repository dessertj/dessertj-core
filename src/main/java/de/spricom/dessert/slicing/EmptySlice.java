package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;

import java.util.Collections;
import java.util.Set;

class EmptySlice extends AbstractSlice {
    @Override
    public Slice with(Slice other) {
        return other;
    }

    @Override
    public Slice without(Slice other) {
        return this;
    }

    @Override
    public Slice slice(Predicate<SliceEntry> predicate) {
        return this;
    }

    @Override
    public boolean contains(SliceEntry entry) {
        return false;
    }

    @Override
    public boolean canResolveSliceEntries() {
        return true;
    }

    @Override
    public Set<SliceEntry> getSliceEntries() {
        return Collections.emptySet();
    }
}
