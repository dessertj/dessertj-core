package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;

public interface SliceSet {
    SliceSet with(SliceSet other);

    SliceSet without(SliceSet other);

    SliceSet slice(Predicate<SliceEntry> predicate);

    boolean contains(SliceEntry entry);
}
