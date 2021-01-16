package de.spricom.dessert.assertions;

import de.spricom.dessert.slicing.Clazz;

import java.util.*;

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
