package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;

import java.util.Set;

public interface Slice {
    Slice with(Slice other);

    Slice without(Slice other);

    Slice slice(Predicate<SliceEntry> predicate);

    boolean contains(SliceEntry entry);

    Set<SliceEntry> getSliceEntries();
}
