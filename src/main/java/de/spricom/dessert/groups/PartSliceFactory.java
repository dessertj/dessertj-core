package de.spricom.dessert.groups;

import de.spricom.dessert.slicing.SliceEntry;

import java.util.Set;

public interface PartSliceFactory<S extends PartSlice> {
    S createPartSlice(Set<SliceEntry> entries, String partKey);
}
