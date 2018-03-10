package de.spricom.dessert.cycledtion;

import de.spricom.dessert.slicing.ConcreteSlice;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.SliceEntry;
import de.spricom.dessert.util.DependencyGraph;
import de.spricom.dessert.util.SetHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SliceGroupAssert {
    private final SliceGroup sliceGroup;

    public SliceGroupAssert(SliceGroup sliceGroup) {
        this.sliceGroup = sliceGroup;
    }

    public SliceGroupAssert isCycleFree() {
        Map<ConcreteSlice, Set<SliceEntry>> dependencies = mapDependencies();
        DependencyGraph<ConcreteSlice> dag = new DependencyGraph<ConcreteSlice>();
        for (ConcreteSlice n : sliceGroup) {
            for (ConcreteSlice m : sliceGroup) {
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

    private Map<ConcreteSlice, Set<SliceEntry>> mapDependencies() {
        Map<ConcreteSlice, Set<SliceEntry>> dependencies = new HashMap<ConcreteSlice, Set<SliceEntry>>();
        for (ConcreteSlice slice : sliceGroup) {
            dependencies.put(slice, getDependencies(slice));
        }
        return dependencies;
    }

    private Set<SliceEntry> getDependencies(ConcreteSlice slice) {
        Set<SliceEntry> dependencies = new HashSet<SliceEntry>();
        for (SliceEntry entry : slice.getSliceEntries()) {
            dependencies.addAll(entry.getUsedClasses());
        }
        return dependencies;
    }

    private String renderCycle(DependencyGraph<ConcreteSlice> dag) {
        StringBuilder sb = new StringBuilder("Cycle:\n");
        int count = 0;
        for (Slice n : dag.getCycle()) {
            sb.append(count == 0 ? "" : ",\n");
            sb.append(n.toString());
            count++;
        }
        return sb.toString();
    }

}
