package de.spricom.dessert.slicing;

import de.spricom.dessert.util.DependencyGraph;
import de.spricom.dessert.util.SetHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SliceSetAssert {
    private final ManifestSliceSet set;

    public SliceSetAssert(ManifestSliceSet set) {
        this.set = set;
    }

    public SliceSetAssert isCycleFree() {
        DependencyGraph<Slice> dag = new DependencyGraph<Slice>();
        for (Slice n : set) {
            for (Slice m : set) {
                if (n != m && n.isUsing(m)) {
                    dag.addDependency(n, m);
                }
            }
        }
        if (!dag.isCycleFree()) {
            StringBuilder sb = new StringBuilder("Cycle:\n");
            int count = 0;
            for (Slice n : dag.getCycle()) {
                sb.append(count == 0 ? "" : ",\n");
                sb.append(n.getPackageName());
                count++;
            }
            throw new AssertionError(sb.toString());
        }
        return this;
    }

    public SliceSetAssert usesOnly(SliceSet... others) {
        Map<Slice, Set<SliceEntry>> illegalDependencies = new HashMap<Slice, Set<SliceEntry>>();
        for (Slice slice : set) {
            Set<SliceEntry> illegals = new HashSet<SliceEntry>();
            for (SliceEntry entry : slice.getUsedClasses()) {
                if (!set.contains(entry) && !containedByAny(entry, others)) {
                    illegals.add(entry);
                }
            }
            if (!illegals.isEmpty()) {
                illegalDependencies.put(slice, illegals);
            }
        }
        if (!illegalDependencies.isEmpty()) {
            throw new AssertionError("Illegal Dependencies:\n" + renderDependencies(illegalDependencies));
        }
        return this;
    }

    public SliceSetAssert doesNotUse(SliceSet... others) {
        Map<Slice, Set<SliceEntry>> illegalDependencies = new HashMap<Slice, Set<SliceEntry>>();
        for (Slice slice : set) {
            Set<SliceEntry> illegals = new HashSet<SliceEntry>();
            for (SliceEntry entry : slice.getUsedClasses()) {
                if (containedByAny(entry, others)) {
                    illegals.add(entry);
                }
            }
            if (!illegals.isEmpty()) {
                illegalDependencies.put(slice, illegals);
            }
        }
        if (!illegalDependencies.isEmpty()) {
            throw new AssertionError("Illegal Dependencies:\n" + renderDependencies(illegalDependencies));
        }
        return this;
    }

    private boolean containedByAny(SliceEntry entry, SliceSet[] sets) {
        for (SliceSet sliceSet : sets) {
            if (sliceSet.contains(entry)) {
                return true;
            }
        }
        return false;
    }

    public SliceSetUsage uses(SliceSet other) {
        return new SliceSetUsage(this, other);
    }

    private String renderDependencies(Map<Slice, Set<SliceEntry>> deps) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Slice, Set<SliceEntry>> entry : deps.entrySet()) {
            for (SliceEntry sliceEntry : entry.getKey().getEntries()) {
                if (SetHelper.containsAny(entry.getValue(), sliceEntry.getUsedClasses())) {
                    sb.append(sliceEntry.getClassname()).append("\n");
                    Set<SliceEntry> illegal = SetHelper.intersect(sliceEntry.getUsedClasses(), entry.getValue());
                    for (SliceEntry illegalDependency : illegal) {
                        sb.append(" -> ").append(illegalDependency).append("\n");
                    }
                }
            }
        }
        return sb.toString();
    }
}
