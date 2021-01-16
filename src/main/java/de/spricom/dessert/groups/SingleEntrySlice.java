package de.spricom.dessert.groups;

import de.spricom.dessert.slicing.Clazz;

import java.util.Collections;

public class SingleEntrySlice extends PartSlice {

    SingleEntrySlice(Clazz entry) {
        super(Collections.singleton(entry), entry.getName());
    }

    public String getClassname() {
        return getPartKey();
    }
}
