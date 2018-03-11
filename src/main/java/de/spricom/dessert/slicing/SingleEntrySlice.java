package de.spricom.dessert.slicing;

import de.spricom.dessert.slicing.ConcreteSlice;
import de.spricom.dessert.slicing.SliceEntry;

import java.util.Collections;

public class SingleEntrySlice extends ConcreteSlice {

    SingleEntrySlice(SliceEntry entry) {
        super(Collections.singleton(entry));
    }

    public String toString() {
        return "class " + getSliceEntries().iterator().next().getClassname();
    }
}
