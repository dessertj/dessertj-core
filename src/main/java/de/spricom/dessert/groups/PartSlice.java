package de.spricom.dessert.groups;

import de.spricom.dessert.slicing.DeferredSlice;
import de.spricom.dessert.slicing.SliceEntry;

import java.util.Set;

public class PartSlice extends DeferredSlice {
    private final String partKey;

    public PartSlice(Set<SliceEntry> sliceEntries, String partKey) {
        super(sliceEntries, partKey);
        this.partKey = partKey;
    }

    public String getPartKey() {
        return partKey;
    }
}
