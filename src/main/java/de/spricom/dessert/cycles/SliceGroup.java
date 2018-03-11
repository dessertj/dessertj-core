package de.spricom.dessert.cycles;

import de.spricom.dessert.slicing.ConcreteSlice;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.SliceEntry;

import java.util.*;

/**
 * A Slice is as Set of the slices for which the elements of each
 * {@link PackageSlice} have common properties. I. e. they belong to the same parent
 * package, the same root, implement the same interface, comply with the same
 * naming convention etc.
 */
public final class SliceGroup<S extends ConcreteSlice> implements Iterable<S> {
    private final Map<String, S> slices;

    public static SliceGroup<SingleEntrySlice> splitByEntry(Slice slice) {
        Set<SingleEntrySlice> entries = new HashSet<SingleEntrySlice>();
        for (SliceEntry entry : slice.materialize().getSliceEntries()) {
            entries.add(new SingleEntrySlice(entry));
        }
        return new SliceGroup(entries);
    }

    public static SliceGroup<PackageSlice> splitByPackage(Slice slice) {
        Map<String, Set<SliceEntry>> packages = new HashMap<String, Set<SliceEntry>>();
        for (SliceEntry entry : slice.materialize().getSliceEntries()) {
            Set<SliceEntry> packageEntries = packages.get(entry.getPackageName());
            if (packageEntries == null) {
                packageEntries = new HashSet<SliceEntry>();
                packages.put(entry.getPackageName(), packageEntries);
            }
            packageEntries.add(entry);
        }
        Set<PackageSlice> entries = new HashSet<PackageSlice>();
        for (Map.Entry<String, Set<SliceEntry>> packageEntry : packages.entrySet()) {
            entries.add(new PackageSlice(packageEntry.getKey(), packageEntry.getValue()));
        }
        return new SliceGroup(entries);
    }

    private SliceGroup(Set<S> members) {
        slices = new TreeMap<String, S>();
        for (S slice : members) {
            if (slices.put(slice.toString(), slice) != null) {
                throw new IllegalArgumentException(slice.toString() + " is not unique");
            }
        }
    }

    @Override
    public Iterator<S> iterator() {
        return slices.values().iterator();
    }

    public S getByName(String name) {
        return slices.get(name);
    }
}
