package de.spricom.dessert.assertions;

import de.spricom.dessert.groups.*;
import de.spricom.dessert.slicing.*;

public class SliceAssert {
    private final Slice slice;
    private IllegalDependenciesRenderer violationsRenderer = new DefaultIllegalDependenciesRenderer();

    SliceAssert(Slice slice) {
        this.slice = slice;
    }

    SliceAssert(SliceAssert previous) {
        this(previous.slice);
        this.violationsRenderer = previous.violationsRenderer;
    }

    public SliceAssert renderWith(IllegalDependenciesRenderer renderer) {
        this.violationsRenderer = renderer;
        return this;
    }

    public SliceAssert usesOnly(Slice... others) {
        IllegalDependencies illegalDependencies = new IllegalDependencies();
        for (Clazz entry : slice.getSliceEntries()) {
            for (Clazz dependency : entry.getDependencies()) {
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
        for (Clazz entry : slice.getSliceEntries()) {
            for (Clazz dependency : entry.getDependencies()) {
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

    private boolean containedByAny(Clazz entry, Slice[] sets) {
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

    public SliceGroupAssert<PackageSlice> splitByPackage() {
        return new SliceGroupAssert<PackageSlice>(this, SliceGroup.splitByPackage(slice));
    }

    public SliceGroupAssert<SingleEntrySlice> splitByEntry() {
        return new SliceGroupAssert<SingleEntrySlice>(this, SliceGroup.splitByEntry(slice));
    }

    public SliceGroupAssert<PartSlice> splitBy(SlicePartioner partioner) {
        return new SliceGroupAssert<PartSlice>(this, SliceGroup.splitBy(slice, partioner));
    }
}
