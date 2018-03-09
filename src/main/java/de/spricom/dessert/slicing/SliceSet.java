package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.ClassPredicate;

public interface SliceSet {
    SliceSet with(SliceSet other);

    SliceSet without(SliceSet other);

    SliceSet slice(ClassPredicate<SliceEntry> predicate);

    boolean contains(SliceEntry entry);
}
