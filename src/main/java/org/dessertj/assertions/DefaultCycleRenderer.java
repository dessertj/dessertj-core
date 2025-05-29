package org.dessertj.assertions;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2025 Hans Jörg Heßmann
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

import org.dessertj.slicing.Clazz;
import org.dessertj.slicing.PackageSlice;
import org.dessertj.slicing.Slice;
import org.dessertj.util.Dag;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * The default implementation used by dessertj-core.
 */
public class DefaultCycleRenderer implements CycleRenderer {

    @Override
    public String renderCycle(Dag<Slice> dag) {
        StringBuilder sb = new StringBuilder("Cycle detected:\n");
        List<Slice> cycle = dag.cycle();
        Slice repeated = cycle.get(cycle.size() - 1);
        Slice m = null;
        boolean withinCycle = false;
        for (Slice n : cycle) {
            if (m == repeated) {
                withinCycle = true;
            }
            if (m != null && withinCycle) {
                renderDependencies(sb, m, n);
            }
            m = n;
        }
        return sb.toString();
    }

    private void renderDependencies(StringBuilder sb, Slice m, Slice n) {
        if (m instanceof Clazz && n instanceof Clazz) {
            sb.append(((Clazz)m).getName()).append(" -> ")
                    .append(((Clazz)n).getName()).append("\n");
            return;
        }
        if (m instanceof PackageSlice && n instanceof PackageSlice) {
            sb.append(((PackageSlice)m).getPackageName()).append(" -> ")
                    .append(((PackageSlice)n).getPackageName()).append(":\n");
        } else {
            sb.append(m).append(" -> ").append(n).append(":\n");
        }

        for (Map.Entry<Clazz, Collection<Clazz>> entry : determineDependencies(m, n).entrySet()) {
            sb.append("\t").append(shortName(entry.getKey())).append(" -> ");
            boolean first = true;
            for (Clazz dep : entry.getValue()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append(shortName(dep));
            }
            sb.append("\n");
        }
    }

    private String shortName(Clazz clazz) {
        if (clazz.getPackageName().isEmpty()) {
            return clazz.getName();
        }
        return clazz.getName().substring(clazz.getPackageName().length() + 1);
    }

    private Map<Clazz, Collection<Clazz>> determineDependencies(Slice m, Slice n) {
        TreeMap<Clazz, Collection<Clazz>> dependencies = new TreeMap<Clazz, Collection<Clazz>>();
        for (Clazz clazz : m.getClazzes()) {
            for (Clazz dep : clazz.getDependencies().getClazzes()) {
                if (n.contains(dep)) {
                    Collection<Clazz> deps = dependencies.get(clazz);
                    if (deps == null) {
                        deps = new TreeSet<Clazz>();
                        dependencies.put(clazz, deps);
                    }
                    deps.add(dep);
                }
            }
        }
        return dependencies;
    }
}
