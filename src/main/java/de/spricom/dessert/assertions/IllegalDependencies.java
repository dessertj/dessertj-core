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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class IllegalDependencies {
    private final Map<Clazz, Set<Clazz>> violations = new HashMap<Clazz, Set<Clazz>>();

    public void add(Clazz entry, Clazz illegalDependency) {
        Set<Clazz> deps = violations.get(entry);
        if (deps == null) {
            deps = new TreeSet<Clazz>();
            violations.put(entry, deps);
        }
        deps.add(illegalDependency);
    }

    public void add(Clazz entry, Set<Clazz> illegalDependencies ) {
        Set<Clazz> deps = violations.get(entry);
        if (deps == null) {
            deps = new TreeSet<Clazz>();
            violations.put(entry, deps);
        }
        deps.addAll(illegalDependencies);
    }

    public Map<Clazz, Set<Clazz>> getViolations() {
        return violations;
    }

    public boolean isEmpty() {
        return violations.isEmpty();
    }
}
