package de.spricom.dessert.assertions;

/*-
 * #%L
 * Dessert Dependency Assertion Library
 * %%
 * Copyright (C) 2017 - 2021 Hans Jörg Heßmann
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
        for (Clazz entry : union.getClazzes()) {
            for (Clazz dependency : entry.getDependencies().getClazzes()) {
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
     * @param others the slices to check dependencies for
     * @return this {@link SliceAssert}
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
    public SliceAssert usesNot(Iterable<Slice> others) {
        IllegalDependencies illegalDependencies = new IllegalDependencies();
        addIllegalDependencies(illegalDependencies, union, others);
        if (!illegalDependencies.isEmpty()) {
            throw new AssertionError(violationsRenderer.render(illegalDependencies));
        }
        return this;
    }

    /**
     * @param others the slices to check dependencies for
     * @return this {@link SliceAssert}
     * @see #usesNot(Iterable)
     */
    public SliceAssert usesNot(Slice... others) {
        return usesNot(Arrays.asList(others));
    }

    private void addIllegalDependencies(IllegalDependencies illegalDependencies, Slice slice, Iterable<Slice> illegals) {
        for (Clazz clazz : slice.getClazzes()) {
            for (Clazz dependency : clazz.getDependencies().getClazzes()) {
                if (containedByAny(dependency, illegals)) {
                    illegalDependencies.add(clazz, dependency);
                }
            }
        }
    }

    private boolean containedByAny(Clazz clazz, Iterable<Slice> sets) {
        for (Slice slice : sets) {
            if (slice.contains(clazz)) {
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
        Map<Slice, ConcreteSlice> dependencies = mapDependencies();
        Dag<Slice> dag = new Dag<Slice>();
        for (Slice n : slices) {
            for (Slice m : slices) {
                if (n.uses(m)) {
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
