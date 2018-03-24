package de.spricom.dessert.groups;

import de.spricom.dessert.slicing.ConcreteSlice;
import de.spricom.dessert.slicing.SliceEntry;

import java.util.Set;

public class PartSlice extends ConcreteSlice {
    private final String partKey;

    public PartSlice(Set<SliceEntry> sliceEntries, String partKey) {
        super(sliceEntries);
        this.partKey = partKey;
    }

    public String getPartKey() {
        return partKey;
    }
}
