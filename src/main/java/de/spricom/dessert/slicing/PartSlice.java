package de.spricom.dessert.slicing;

import java.util.Set;

public class PartSlice extends ConcreteSlice {
    private final String partKey;

    public PartSlice(String partKey, Set<Clazz> sliceEntries) {
        super(sliceEntries);
        this.partKey = partKey;
    }

    public String getPartKey() {
        return partKey;
    }

    public String toString() {
        return "slice partition " + partKey;
    }
}
