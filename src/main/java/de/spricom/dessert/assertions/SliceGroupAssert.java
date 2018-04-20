package de.spricom.dessert.assertions;

import de.spricom.dessert.groups.PartSlice;
import de.spricom.dessert.groups.SliceGroup;
import de.spricom.dessert.slicing.SliceEntry;
import de.spricom.dessert.util.DependencyGraph;
import de.spricom.dessert.util.SetHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SliceGroupAssert<S extends PartSlice> extends SliceAssert {
    private final SliceGroup<S> sliceGroup;
    private CycleRenderer<S> cycleRenderer = new DefaultCycleRenderer<S>();

    public SliceGroupAssert(SliceAssert sliceAssert, SliceGroup<S> sliceGroup) {
        super(sliceAssert);
        this.sliceGroup = sliceGroup;
    }

    public SliceGroupAssert(SliceGroup<S> sliceGroup) {
        super(sliceGroup.getOriginalSlice());
        this.sliceGroup = sliceGroup;
    }

    public SliceAssert renderWith(CycleRenderer<S> renderer) {
        this.cycleRenderer = renderer;
        return this;
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
        return cycleRenderer.renderCycle(dag);
    }
}
