package de.spricom.dessert.groups;

import de.spricom.dessert.slicing.SliceEntry;

public interface SlicePartioner {
    String partKey(SliceEntry entry);
}
