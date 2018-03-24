package de.spricom.dessert.assertions;

import de.spricom.dessert.groups.PartSlice;
import de.spricom.dessert.groups.SliceGroup;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.SliceEntry;
import de.spricom.dessert.util.DependencyGraph;
import de.spricom.dessert.util.SetHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SliceGroupAssert<S extends PartSlice> {
    private final SliceAssert sliceAssert;
    private final SliceGroup<S> sliceGroup;

    public SliceGroupAssert(SliceAssert sliceAssert, SliceGroup<S> sliceGroup) {
        this.sliceAssert = sliceAssert;
        this.sliceGroup = sliceGroup;
    }

    public SliceGroupAssert isCycleFree() {
        Map<S, Set<SliceEntry>> dependencies = mapDependencies();
        DependencyGraph<S> dag = new DependencyGraph<S>();
        for (S n : sliceGroup) {
            for (S m : sliceGroup) {
                if (n != m && SetHelper.containsAny(dependencies.get(n), m.getSliceEntries())) {
                    dag.addDependency(n, m);
                }
            }
        }
        if (!dag.isCycleFree()) {
            String cycle = renderCycle(dag);
            throw new AssertionError(cycle);
        }
        return this;
    }

    private Map<S, Set<SliceEntry>> mapDependencies() {
        Map<S, Set<SliceEntry>> dependencies = new HashMap<S, Set<SliceEntry>>();
        for (S slice : sliceGroup) {
            dependencies.put(slice, getDependencies(slice));
        }
        return dependencies;
    }

    private Set<SliceEntry> getDependencies(S slice) {
        Set<SliceEntry> dependencies = new HashSet<SliceEntry>();
        for (SliceEntry entry : slice.getSliceEntries()) {
            dependencies.addAll(entry.getUsedClasses());
        }
        return dependencies;
    }

    private String renderCycle(DependencyGraph<S> dag) {
        StringBuilder sb = new StringBuilder("Cycle:\n");
        int count = 0;
        for (Slice n : dag.getCycle()) {
            sb.append(count == 0 ? "" : ",\n");
            sb.append(n.toString());
            count++;
        }
        return sb.toString();
    }

    public SliceAssert end() {
        return sliceAssert;
    }
}
