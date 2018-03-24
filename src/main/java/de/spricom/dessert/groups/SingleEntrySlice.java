package de.spricom.dessert.groups;

import de.spricom.dessert.slicing.SliceEntry;

import java.util.Collections;

public class SingleEntrySlice extends PartSlice {

    SingleEntrySlice(SliceEntry entry) {
        super(Collections.singleton(entry), entry.getClassname());
    }

    public String getClassname() {
        return getPartKey();
    }
}
