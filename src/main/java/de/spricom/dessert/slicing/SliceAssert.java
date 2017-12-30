package de.spricom.dessert.slicing;

import java.util.HashSet;
import java.util.Set;

public class SliceAssert {
    private final Slice slice;

    public SliceAssert(Slice slice) {
        this.slice = slice;
    }

    public void doesNotUse(Slice pckg) {
        Set<SliceEntry> deps = new HashSet<SliceEntry>();
        for (SliceEntry entry : slice.getEntries()) {
            deps.addAll(entry.getUsedClasses());
        }
        deps.retainAll(pckg.getEntries());
        if (!deps.isEmpty()) {
            throw new AssertionError(slice.getPackageName() + " uses " + deps);
        }
    }

}
