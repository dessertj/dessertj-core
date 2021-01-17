package de.spricom.dessert.slicing;

import java.util.Map;
import java.util.Set;

public interface PartSliceFactory<S extends PartSlice> {
    S createPartSlice(String partKey, Set<Clazz> entries, Map<String, S> slices);
}
