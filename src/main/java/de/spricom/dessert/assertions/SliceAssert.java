package de.spricom.dessert.assertions;

import de.spricom.dessert.slicing.Clazz;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.Slices;
import de.spricom.dessert.util.DependencyGraph;
import de.spricom.dessert.util.SetHelper;

import java.util.*;

public class SliceAssert {
    private final Iterable<? extends Slice> slices;
    private final Slice union;
    private IllegalDependenciesRenderer violationsRenderer = new DefaultIllegalDependenciesRenderer();
    private CycleRenderer cycleRenderer = new DefaultCycleRenderer();

    SliceAssert(Iterable<? extends Slice> slices) {
        this.slices = slices;
        this.union = Slices.of(slices);
    }

    public SliceAssert renderWith(IllegalDependenciesRenderer renderer) {
        this.violationsRenderer = renderer;
        return this;
    }

    public SliceAssert renderCycleWith(CycleRenderer renderer) {
        this.cycleRenderer = renderer;
        return this;
    }

    /**
     * Assert the given slices have no other dependencies than <i>others</i>.
     *
     * @param others the slices to check dependencies for
     * @return this {@link SliceAssert}
     */
    public SliceAssert usesOnly(Iterable<Slice> others) {
        IllegalDependencies illegalDependencies = new IllegalDependencies();
        for (Clazz entry : union.getSliceEntries()) {
            for (Clazz dependency : entry.getDependencies()) {
                if (!union.contains(dependency) && !containedByAny(dependency, others)) {
                    illegalDependencies.add(entry, dependency);
                }
            }
        }
        if (!illegalDependencies.isEmpty()) {
            throw new AssertionError(violationsRenderer.render(illegalDependencies));
        }
        return this;
    }

    /**
     * @see #usesOnly(Iterable)
     */
    public SliceAssert usesOnly(Slice... others) {
        return usesOnly(Arrays.asList(others));
    }

    /**
     * Assert the given slices have dependencies to <i>others</i>.
     *
     * @param others the slices to check dependencies for
     * @return this {@link SliceAssert}
     */
    public SliceAssert doesNotUse(Iterable<Slice> others) {
        IllegalDependencies illegalDependencies = new IllegalDependencies();
        addIllegalDependencies(illegalDependencies, union, others);
        if (!illegalDependencies.isEmpty()) {
            throw new AssertionError(violationsRenderer.render(illegalDependencies));
        }
        return this;
    }

    /**
     * @see #doesNotUse(Iterable)
     */
    public SliceAssert doesNotUse(Slice... others) {
        return doesNotUse(Arrays.asList(others));
    }

    private void addIllegalDependencies(IllegalDependencies illegalDependencies, Slice slice, Iterable<Slice> illegals) {
        for (Clazz entry : slice.getSliceEntries()) {
            for (Clazz dependency : entry.getDependencies()) {
                if (containedByAny(dependency, illegals)) {
                    illegalDependencies.add(entry, dependency);
                }
            }
        }
    }

    private boolean containedByAny(Clazz entry, Iterable<Slice> sets) {
        for (Slice slice : sets) {
            if (slice.contains(entry)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Assert there are no cyclic dependencies
     * @return this {@link SliceAssert}
     */
    public SliceAssert isCycleFree() {
        Map<Slice, Set<Clazz>> dependencies = mapDependencies();
        DependencyGraph<Slice> dag = new DependencyGraph<Slice>();
        for (Slice n : slices) {
            for (Slice m : slices) {
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

    /**
     * Assert there are no backward references and each slice uses only its direct successor.
     * @return this {@link SliceAssert}
     */
    public SliceAssert isLayeredStrict() {
        IllegalDependencies illegalDependencies = new IllegalDependencies();
        List<Slice> list = asList();
        if (list.size() < 2) {
            return this;
        }

        for (int i = list.size() - 1; i > 0; i--) {
            // disallow backward dependencies
            addIllegalDependencies(illegalDependencies, list.get(i), list.subList(0, i));
            // disallow forward dependencies skipping one layer
            if (i + 2 < list.size()) {
                addIllegalDependencies(illegalDependencies, list.get(i), list.subList(i + 2, list.size()));
            }
        }

        if (!illegalDependencies.isEmpty()) {
            throw new AssertionError(violationsRenderer.render(illegalDependencies));
        }
        return this;
    }

    /**
     * Assert there are no backward references.
     * @return this {@link SliceAssert}
     */
    public SliceAssert isLayeredRelaxed() {
        IllegalDependencies illegalDependencies = new IllegalDependencies();
        List<Slice> list = asList();
        if (list.size() < 2) {
            return this;
        }

        for (int i = list.size() - 1; i > 0; i--) {
            // disallow backward dependencies
            addIllegalDependencies(illegalDependencies, list.get(i), list.subList(0, i));
        }

        if (!illegalDependencies.isEmpty()) {
            throw new AssertionError(violationsRenderer.render(illegalDependencies));
        }
        return this;
    }

    private List<Slice> asList() {
        List list = new ArrayList();
        for (Slice slice : slices) {
            list.add(slice);
        }
        return list;
    }

    private Map<Slice, Set<Clazz>> mapDependencies() {
        Map<Slice, Set<Clazz>> dependencies = new HashMap<Slice, Set<Clazz>>();
        for (Slice slice : slices) {
            dependencies.put(slice, getDependencies(slice));
        }
        return dependencies;
    }

    private Set<Clazz> getDependencies(Slice slice) {
        Set<Clazz> dependencies = new HashSet<Clazz>();
        for (Clazz entry : slice.getSliceEntries()) {
            dependencies.addAll(entry.getDependencies());
        }
        return dependencies;
    }

    private String renderCycle(DependencyGraph<Slice> dag) {
        return cycleRenderer.renderCycle(dag);
    }
}
