package de.spricom.dessert.slicing;

import de.spricom.dessert.util.DependencyGraph;
import de.spricom.dessert.util.SetHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SliceAssert {
    private final ConcreteSlice set;

    public SliceAssert(ConcreteSlice set) {
        this.set = set;
    }

    public SliceAssert isCycleFree() {
        DependencyGraph<PackageSlice> dag = new DependencyGraph<PackageSlice>();
        for (PackageSlice n : set) {
            for (PackageSlice m : set) {
                if (n != m && n.isUsing(m)) {
                    dag.addDependency(n, m);
                }
            }
        }
        if (!dag.isCycleFree()) {
            StringBuilder sb = new StringBuilder("Cycle:\n");
            int count = 0;
            for (PackageSlice n : dag.getCycle()) {
                sb.append(count == 0 ? "" : ",\n");
                sb.append(n.getPackageName());
                count++;
            }
            throw new AssertionError(sb.toString());
        }
        return this;
    }

    public SliceAssert usesOnly(Slice... others) {
        Map<PackageSlice, Set<SliceEntry>> illegalDependencies = new HashMap<PackageSlice, Set<SliceEntry>>();
        for (PackageSlice packageSlice : set) {
            Set<SliceEntry> illegals = new HashSet<SliceEntry>();
            for (SliceEntry entry : packageSlice.getUsedClasses()) {
                if (!set.contains(entry) && !containedByAny(entry, others)) {
                    illegals.add(entry);
                }
            }
            if (!illegals.isEmpty()) {
                illegalDependencies.put(packageSlice, illegals);
            }
        }
        if (!illegalDependencies.isEmpty()) {
            throw new AssertionError("Illegal Dependencies:\n" + renderDependencies(illegalDependencies));
        }
        return this;
    }

    public SliceAssert doesNotUse(Slice... others) {
        Map<PackageSlice, Set<SliceEntry>> illegalDependencies = new HashMap<PackageSlice, Set<SliceEntry>>();
        for (PackageSlice packageSlice : set) {
            Set<SliceEntry> illegals = new HashSet<SliceEntry>();
            for (SliceEntry entry : packageSlice.getUsedClasses()) {
                if (containedByAny(entry, others)) {
                    illegals.add(entry);
                }
            }
            if (!illegals.isEmpty()) {
                illegalDependencies.put(packageSlice, illegals);
            }
        }
        if (!illegalDependencies.isEmpty()) {
            throw new AssertionError("Illegal Dependencies:\n" + renderDependencies(illegalDependencies));
        }
        return this;
    }

    private boolean containedByAny(SliceEntry entry, Slice[] sets) {
        for (Slice slice : sets) {
            if (slice.contains(entry)) {
                return true;
            }
        }
        return false;
    }

    public SliceUsage uses(Slice other) {
        return new SliceUsage(this, other);
    }

    private String renderDependencies(Map<PackageSlice, Set<SliceEntry>> deps) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<PackageSlice, Set<SliceEntry>> entry : deps.entrySet()) {
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
