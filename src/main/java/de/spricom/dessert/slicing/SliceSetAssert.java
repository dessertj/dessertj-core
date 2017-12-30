package de.spricom.dessert.slicing;

import de.spricom.dessert.util.DependencyGraph;
import de.spricom.dessert.util.SetHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SliceSetAssert {
    private final SliceSet set;

    public SliceSetAssert(SliceSet set) {
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

    public SliceSetAssert usesOnly(SliceSet... other) {
        Set<SliceEntry> allowedEntries = entries(other);
        allowedEntries.addAll(entries(set));
        Map<Slice, Set<SliceEntry>> illegalDependencies = new HashMap<Slice, Set<SliceEntry>>();
        for (Slice s : set) {
            if (!SetHelper.containsAll(allowedEntries, s.getUsedClasses())) {
                illegalDependencies.put(s, SetHelper.subtract(s.getUsedClasses(), allowedEntries));
            }
        }
        if (!illegalDependencies.isEmpty()) {
            throw new AssertionError("Illegal Dependencies:\n" + renderDependencies(illegalDependencies));
        }
        return this;
    }

    public SliceSetAssert doesNotUse(SliceSet... other) {
        Set<SliceEntry> disallowedEntries = entries(other);
        Map<Slice, Set<SliceEntry>> illegalDependencies = new HashMap<Slice, Set<SliceEntry>>();
        for (Slice s : set) {
            if (SetHelper.containsAny(s.getUsedClasses(), disallowedEntries)) {
                illegalDependencies.put(s, SetHelper.intersect(s.getUsedClasses(), disallowedEntries));
            }
        }
        if (!illegalDependencies.isEmpty()) {
            throw new AssertionError("Illegal Dependencies:\n" + renderDependencies(illegalDependencies));
        }
        return this;
    }

    public SliceSetUsage uses(SliceSet other) {
        return new SliceSetUsage(this, other);
    }

    private Set<SliceEntry> entries(SliceSet... sliceSets) {
        HashSet<SliceEntry> entries = new HashSet<SliceEntry>();
        for (SliceSet sliceSet : sliceSets) {
            for (Slice s : sliceSet) {
                entries.addAll(s.getEntries());
            }
        }
        return entries;
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
