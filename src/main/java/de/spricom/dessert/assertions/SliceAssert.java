package de.spricom.dessert.assertions;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2022 Hans Jörg Heßmann
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import de.spricom.dessert.slicing.Clazz;
import de.spricom.dessert.slicing.ConcreteSlice;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.Slices;
import de.spricom.dessert.util.Dag;

import java.util.*;

/**
 * Implements a DSL for slice assertions using a fluent API.
 */
public class SliceAssert {
    private final Iterable<? extends Slice> slices;
    private final Slice union;
    private IllegalDependenciesRenderer violationsRenderer = new DefaultIllegalDependenciesRenderer();
    private CycleRenderer cycleRenderer = new DefaultCycleRenderer();

    SliceAssert(Iterable<? extends Slice> slices) {
        this.slices = slices;
        this.union = Slices.of(slices);
    }

    /**
     * Use custom renderer to produces the {@link AssertionError} message
     * for dependency violations.
     *
     * @param renderer the renderer
     * @return this instance (fluent API)
     */
    public SliceAssert renderWith(IllegalDependenciesRenderer renderer) {
        this.violationsRenderer = renderer;
        return this;
    }

    /**
     * Use custom renderer to produces the {@link AssertionError} message
     * for a detected cycle.
     *
     * @param renderer the renderer
     * @return this instance (fluent API)
     */
    public SliceAssert renderCycleWith(CycleRenderer renderer) {
        this.cycleRenderer = renderer;
        return this;
    }

    /**
     * Assert the current slices have no other dependencies than those contained by the slices
     * passed to this method.
     *
     * @param others the slices to check dependencies for
     * @return this instance (fluent API)
     */
    public SliceAssert usesOnly(Iterable<Slice> others) {
        IllegalDependencies illegalDependencies = new IllegalDependencies();
        for (Clazz entry : union.getClazzes()) {
            for (Clazz dependency : entry.getDependencies().getClazzes()) {
                if (!union.contains(dependency) && !containsAny(others, dependency)) {
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
     * Plural alias for {@link #usesOnly(Iterable)}.
     *
     * @param others the slices to check dependencies for
     * @return this instance (fluent API)
     */
    public SliceAssert useOnly(Iterable<Slice> others) {
        return usesOnly(others);
    }

    /**
     * Alternative for {@link #usesOnly(Iterable)}.
     *
     * @param others the slices to check dependencies for
     * @return this instance (fluent API)
     * @see #usesOnly(Iterable)
     */
    public SliceAssert usesOnly(Slice... others) {
        return usesOnly(Arrays.asList(others));
    }

    /**
     * Plural alias for {@link #usesOnly(Iterable)}.
     *
     * @param others the slices to check dependencies for
     * @return this instance (fluent API)
     */
    public SliceAssert useOnly(Slice... others) {
        return usesOnly(others);
    }

    /**
     * Assert the current slices have no dependency to any class contained by the slices
     * passed to this method.
     *
     * @param others the slices to check dependencies for
     * @return this instance (fluent API)
     */
    public SliceAssert usesNot(Iterable<Slice> others) {
        IllegalDependencies illegalDependencies = new IllegalDependencies();
        addIllegalDependencies(illegalDependencies, union, others);
        if (!illegalDependencies.isEmpty()) {
            throw new AssertionError(violationsRenderer.render(illegalDependencies));
        }
        return this;
    }

    /**
     * Alternative for {@link #usesNot(Iterable)}.
     *
     * @param others the slices to check dependencies for
     * @return this instance (fluent API)
     * @see #usesNot(Iterable)
     */
    public SliceAssert usesNot(Slice... others) {
        return usesNot(Arrays.asList(others));
    }

    /**
     * Alias for {@link #usesNot(Iterable)}
     *
     * @param others the slices to check dependencies for
     * @return this instance (fluent API)
     */
    public SliceAssert doesNotUse(Iterable<Slice> others) {
        return usesNot(others);
    }

    /**
     * Alias for {@link #usesNot(Slice...)}
     *
     * @param others the slices to check dependencies for
     * @return this instance (fluent API)
     */
    public SliceAssert doesNotUse(Slice... others) {
        return usesNot(others);
    }

    /**
     * Plural alias for {@link #usesNot(Iterable)}
     *
     * @param others the slices to check dependencies for
     * @return this instance (fluent API)
     */
    public SliceAssert doNotUse(Iterable<Slice> others) {
        return usesNot(others);
    }

    /**
     * Plural alias for {@link #usesNot(Slice...)}
     *
     * @param others the slices to check dependencies for
     * @return this instance (fluent API)
     */
    public SliceAssert doNotUse(Slice... others) {
        return usesNot(others);
    }

    private void addIllegalDependencies(IllegalDependencies illegalDependencies, Slice slice, Iterable<Slice> illegals) {
        for (Clazz clazz : slice.getClazzes()) {
            for (Clazz dependency : clazz.getDependencies().getClazzes()) {
                if (containsAny(illegals, dependency)) {
                    illegalDependencies.add(clazz, dependency);
                }
            }
        }
    }

    private boolean containsAny(Iterable<Slice> slices, Clazz clazz) {
        for (Slice slice : slices) {
            if (slice.contains(clazz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Assert there are no cyclic dependencies.
     *
     * @return this instance (fluent API)
     */
    public SliceAssert isCycleFree() {
        Map<Slice, ConcreteSlice> dependencies = mapDependencies();
        Dag<Slice> dag = new Dag<Slice>();
        for (Slice n : slices) {
            for (Slice m : slices) {
                if (n != m && n.uses(m)) {
                    dag.addEdge(n, m);
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
     * Plural alias for {@link #isCycleFree()}.
     *
     * @return this instance (fluent API)
     */
    public SliceAssert areCycleFree() {
        return isCycleFree();
    }

    /**
     * Assert there are no backward references and each slice uses only its direct successor.
     *
     * @return this instance (fluent API)
     */
    public SliceAssert isLayeredStrict() {
        IllegalDependencies illegalDependencies = new IllegalDependencies();
        List<Slice> list = asList();
        if (list.size() < 2) {
            return this;
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            // disallow backward dependencies
            if (i > 0) {
                addIllegalDependencies(illegalDependencies, list.get(i), list.subList(0, i));
            }
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
     * Plural alias for {@link #isLayeredStrict()}.
     *
     * @return this instance (fluent API)
     */
    public SliceAssert areLayeredStrict() {
        return isLayeredStrict();
    }

    /**
     * Assert there are no backward references.
     *
     * @return this instance (fluent API)
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

    /**
     * Plural alias for {@link #isLayeredRelaxed()} ()}.
     *
     * @return this instance (fluent API)
     */
    public SliceAssert areLayeredRelaxed() {
        return isLayeredRelaxed();
    }

    private List<Slice> asList() {
        List<Slice> list = new ArrayList<Slice>();
        for (Slice slice : slices) {
            list.add(slice);
        }
        return list;
    }

    private Map<Slice, ConcreteSlice> mapDependencies() {
        Map<Slice, ConcreteSlice> dependencies = new HashMap<Slice, ConcreteSlice>();
        for (Slice slice : slices) {
            dependencies.put(slice, slice.getDependencies());
        }
        return dependencies;
    }

    private String renderCycle(Dag<Slice> dag) {
        return cycleRenderer.renderCycle(dag);
    }
}
