package de.spricom.dessert.slicing;

import java.util.Map;
import java.util.Set;

public interface PartitionSliceFactory<S extends PartitionSlice> {
    S createPartSlice(String partKey, Set<Clazz> entries, Map<String, S> slices);
}
