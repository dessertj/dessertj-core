package de.spricom.dessert.assertions;

import de.spricom.dessert.slicing.ConcreteSlice;
import de.spricom.dessert.slicing.PackageSlice;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.SliceEntry;
import de.spricom.dessert.util.SetHelper;

import java.util.Map;
import java.util.Set;

public class SliceAssert {
    private final ConcreteSlice slice;
    private IllegalDependenciesRenderer violationsRenderer = new DefaultIllegalDependenciesRenderer();

    SliceAssert(ConcreteSlice slice) {
        this.slice = slice;
    }

    public SliceAssert renderWith(IllegalDependenciesRenderer renderer) {
        this.violationsRenderer = renderer;
        return this;
    }

    public SliceAssert usesOnly(Slice... others) {
        IllegalDependencies illegalDependencies = new IllegalDependencies();
        for (SliceEntry entry : slice.getSliceEntries()) {
            for (SliceEntry dependency : entry.getUsedClasses()) {
                if (!slice.contains(dependency) && !containedByAny(dependency, others)) {
                    illegalDependencies.add(entry, dependency);
                }
            }
        }
        if (!illegalDependencies.isEmpty()) {
            throw new AssertionError(violationsRenderer.render(illegalDependencies));
        }
        return this;
    }

    public SliceAssert doesNotUse(Slice... others) {
        IllegalDependencies illegalDependencies = new IllegalDependencies();
        for (SliceEntry entry : slice.getSliceEntries()) {
            for (SliceEntry dependency : entry.getUsedClasses()) {
                if (containedByAny(dependency, others)) {
                    illegalDependencies.add(entry, dependency);
                }
            }
        }
        if (!illegalDependencies.isEmpty()) {
            throw new AssertionError(violationsRenderer.render(illegalDependencies));
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
