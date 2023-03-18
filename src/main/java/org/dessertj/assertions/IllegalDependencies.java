package org.dessertj.assertions;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2023 Hans Jörg Heßmann
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Container for dependency violations during dependency assertion checks.
 */
public class IllegalDependencies {
    private final Map<Clazz, Set<Clazz>> violations = new HashMap<Clazz, Set<Clazz>>();

    /**
     * Add an illegal dependency for a class.
     *
     * @param clazz the class
     * @param illegalDependency the illegal dependency
     */
    public void add(Clazz clazz, Clazz illegalDependency) {
        Set<Clazz> deps = violations.get(clazz);
        if (deps == null) {
            deps = new TreeSet<Clazz>();
            violations.put(clazz, deps);
        }
        deps.add(illegalDependency);
    }

    /**
     * Add a set of illegal dependencies for a class.
     *
     * @param clazz the class
     * @param illegalDependencies the illegal dependencies
     */
    public void add(Clazz clazz, Set<Clazz> illegalDependencies) {
        Set<Clazz> deps = violations.get(clazz);
        if (deps == null) {
            deps = new TreeSet<Clazz>();
            violations.put(clazz, deps);
        }
        deps.addAll(illegalDependencies);
    }

    /**
     * @return all dependency violations
     */
    public Map<Clazz, Set<Clazz>> getViolations() {
        return violations;
    }

    /**
     * @return true if there are no dependency violations
     */
    public boolean isEmpty() {
        return violations.isEmpty();
    }
}
