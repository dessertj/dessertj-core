package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;

import java.util.Set;

/**
 * A Slice is an arbitrary Set of {@link SliceEntry}.
 */
public interface Slice {

    Slice plus(Slice... slices);

    Slice minus(Slice... slices);

    Slice with(Slice other);

    Slice without(Slice other);

    Slice slice(Predicate<SliceEntry> predicate);

    boolean contains(SliceEntry entry);

    boolean canResolveSliceEntries();

    Set<SliceEntry> getSliceEntries();
}
