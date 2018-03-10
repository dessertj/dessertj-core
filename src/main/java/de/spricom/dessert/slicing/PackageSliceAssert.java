package de.spricom.dessert.slicing;

import java.util.HashSet;
import java.util.Set;

public final class PackageSliceAssert {
    private final PackageSlice packageSlice;

    public PackageSliceAssert(PackageSlice packageSlice) {
        this.packageSlice = packageSlice;
    }

    public void doesNotUse(PackageSlice pckg) {
        Set<SliceEntry> deps = new HashSet<SliceEntry>();
        for (SliceEntry entry : packageSlice.getEntries()) {
            deps.addAll(entry.getUsedClasses());
        }
        deps.retainAll(pckg.getEntries());
        if (!deps.isEmpty()) {
            throw new AssertionError(packageSlice.getPackageName() + " uses " + deps);
        }
    }

}
