package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;

import java.util.Collections;
import java.util.Set;

class EmptySlice extends AbstractSlice {

    @Override
    public Slice combine(Slice other) {
        return other;
    }

    @Override
    public Slice slice(Predicate<Clazz> predicate) {
        return this;
    }

    @Override
    public boolean contains(Clazz entry) {
        return false;
    }

    @Override
    public boolean canResolveSliceEntries() {
        return true;
    }

    @Override
    public Set<Clazz> getSliceEntries() {
        return Collections.emptySet();
    }
}
