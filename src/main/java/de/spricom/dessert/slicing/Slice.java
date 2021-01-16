package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;

import java.util.Set;

/**
 * A Slice is an arbitrary Set of {@link Clazz}.
 */
public interface Slice {

    Slice plus(Slice... slices);

    Slice minus(Slice... slices);

    Slice combine(Slice other);

    Slice slice(Predicate<Clazz> predicate);

    boolean contains(Clazz entry);

    boolean canResolveSliceEntries();

    Set<Clazz> getSliceEntries();
}
