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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DefaultIllegalDependenciesRenderer implements IllegalDependenciesRenderer {
    @Override
    public String render(IllegalDependencies violations) {
        Map<Clazz, Set<Clazz>> dependecyViolations = violations.getViolations();
        StringBuilder sb = new StringBuilder("Illegal Dependencies:\n");
        for (Clazz entry : sort(dependecyViolations.keySet())) {
            sb.append(entry.getName()).append("\n");
            for (Clazz dep : dependecyViolations.get(entry)) {
                sb.append(" -> ").append(dep.getName()).append("\n");
            }
        }
        return sb.toString();
    }

    private Collection<Clazz> sort(Set<Clazz> entries) {
        TreeMap<String, Clazz> sorted = new TreeMap<String, Clazz>();
        for (Clazz entry : entries) {
            sorted.put(entry.getName(), entry);
        }
        return sorted.values();
    }
}
