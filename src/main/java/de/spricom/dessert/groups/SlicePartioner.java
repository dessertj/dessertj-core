package de.spricom.dessert.groups;

import de.spricom.dessert.slicing.Clazz;

public interface SlicePartioner {
    String partKey(Clazz entry);
}
