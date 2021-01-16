package de.spricom.dessert.groups;

import de.spricom.dessert.slicing.ConcreteSlice;
import de.spricom.dessert.slicing.Clazz;

import java.util.Set;

public class PartSlice extends ConcreteSlice {
    private final String partKey;

    public PartSlice(Set<Clazz> sliceEntries, String partKey) {
        super(sliceEntries);
        this.partKey = partKey;
    }

    public String getPartKey() {
        return partKey;
    }
}
