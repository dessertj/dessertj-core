package de.spricom.dessert.groups;

import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.SliceEntry;

import java.util.*;

/**
 * A Slice is as Set of the slices for which the elements of each
 * {@link PackageSlice} have common properties. I. e. they belong to the same parent
 * package, the same root, implement the same interface, comply with the same
 * naming convention etc.
 */
public final class SliceGroup<S extends PartSlice> implements Iterable<S> {
    private final Map<String, S> slices;

    public static SliceGroup<SingleEntrySlice> splitByEntry(Slice slice) {
        return new SliceGroup<SingleEntrySlice>(slice,
                new SlicePartioner() {
                    @Override
                    public String partKey(SliceEntry entry) {
                        return entry.getClassname();
                    }
                },
                new PartSliceFactory<SingleEntrySlice>() {
                    @Override
                    public SingleEntrySlice createPartSlice(Set<SliceEntry> entries, String partKey) {
                        assert entries.size() == 1 : entries + " must contain exactly one entry";
                        return new SingleEntrySlice(entries.iterator().next());
                    }
                });
    }

    public static SliceGroup<PackageSlice> splitByPackage(Slice slice) {
        return new SliceGroup<PackageSlice>(slice,
                new SlicePartioner() {
                    @Override
                    public String partKey(SliceEntry entry) {
                        return entry.getPackageName();
                    }
                },
                new PartSliceFactory<PackageSlice>() {
                    @Override
                    public PackageSlice createPartSlice(Set<SliceEntry> entries, String partKey) {
                        return new PackageSlice(partKey, entries);
                    }
                });
    }

    public static SliceGroup<PartSlice> splitBy(Slice slice, SlicePartioner partioner) {
        return new SliceGroup<PartSlice>(slice,
                partioner,
                new PartSliceFactory<PartSlice>() {
                    @Override
                    public PartSlice createPartSlice(Set<SliceEntry> entries, String partKey) {
                        return new PartSlice(entries, partKey);
                    }
                });
    }


    public SliceGroup(Slice slice, SlicePartioner partitioner, PartSliceFactory<S> partSliceFactory) {
        Map<String, Set<SliceEntry>> parts = new HashMap<String, Set<SliceEntry>>();
        for (SliceEntry entry : slice.getSliceEntries()) {
            String partKey = partitioner.partKey(entry);
            Set<SliceEntry> partEntries = parts.get(partKey);
            if (partEntries == null) {
                partEntries = new HashSet<SliceEntry>();
                parts.put(partKey, partEntries);
            }
            partEntries.add(entry);
        }
        slices = new TreeMap<String, S>();
        for (Map.Entry<String, Set<SliceEntry>> part : parts.entrySet()) {
            if (slices.put(part.getKey(), partSliceFactory.createPartSlice(part.getValue(), part.getKey())) != null) {
                throw new IllegalArgumentException(slice.toString() + " is not unique");
            }
        }
    }

    @Override
    public Iterator<S> iterator() {
        return slices.values().iterator();
    }

    public S getByPartKey(String partKey) {
        return slices.get(partKey);
    }
}
