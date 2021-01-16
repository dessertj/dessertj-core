package de.spricom.dessert.groups;

import de.spricom.dessert.slicing.Clazz;

import java.util.Set;

public interface PartSliceFactory<S extends PartSlice> {
    S createPartSlice(Set<Clazz> entries, String partKey);
}
