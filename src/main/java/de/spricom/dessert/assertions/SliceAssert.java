package de.spricom.dessert.assertions;

import de.spricom.dessert.slicing.ConcreteSlice;
import de.spricom.dessert.slicing.PackageSlice;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.SliceEntry;
import de.spricom.dessert.util.DependencyGraph;
import de.spricom.dessert.util.SetHelper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SliceAssert {
    private final ConcreteSlice slice;
    private DependencyViolationsRenderer violationsRenderer = new DefaultDependencyViolationsRenderer();

    SliceAssert(ConcreteSlice slice) {
        this.slice = slice;
    }

    public SliceAssert renderWith(DependencyViolationsRenderer renderer) {
        this.violationsRenderer = renderer;
        return this;
    }

    public SliceAssert isCycleFree() {
        DependencyGraph<PackageSlice> dag = new DependencyGraph<PackageSlice>();
        for (PackageSlice n : slice) {
            for (PackageSlice m : slice) {
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
        DependencyViolations dependencyViolations = new DependencyViolations();
        for (SliceEntry entry : getSliceEntries()) {
            for (SliceEntry dependency : entry.getUsedClasses()) {
                if (!slice.contains(dependency) && !containedByAny(dependency, others)) {
                    dependencyViolations.add(entry, dependency);
                }
            }
        }
        if (!dependencyViolations.isEmpty()) {
            throw new AssertionError(violationsRenderer.render(dependencyViolations));
        }
        return this;
    }

    public SliceAssert doesNotUse(Slice... others) {
        DependencyViolations dependencyViolations = new DependencyViolations();
        for (SliceEntry entry : getSliceEntries()) {
            for (SliceEntry dependency : entry.getUsedClasses()) {
                if (containedByAny(dependency, others)) {
                    dependencyViolations.add(entry, dependency);
                }
            }
        }
        if (!dependencyViolations.isEmpty()) {
            throw new AssertionError(violationsRenderer.render(dependencyViolations));
        }
        return this;
    }

    private Set<SliceEntry> getSliceEntries() {
        Set<SliceEntry> entries = new HashSet<SliceEntry>();
        for (PackageSlice packageSlice : slice) {
            entries.addAll(packageSlice.getEntries());
        }
        return entries;
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
