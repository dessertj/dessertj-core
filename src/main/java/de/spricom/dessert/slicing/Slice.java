package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;

public interface Slice {
    Slice with(Slice other);

    Slice without(Slice other);

    Slice slice(Predicate<SliceEntry> predicate);

    boolean contains(SliceEntry entry);
}
