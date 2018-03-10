package de.spricom.dessert.assertions;

import de.spricom.dessert.slicing.SliceEntry;

import java.util.*;

public class IllegalDependencies {
    private final Map<SliceEntry, Set<SliceEntry>> violations = new HashMap<SliceEntry, Set<SliceEntry>>();

    public void add(SliceEntry entry, SliceEntry illegalDependency) {
        Set<SliceEntry> deps = violations.get(entry);
        if (deps == null) {
            deps = new TreeSet<SliceEntry>();
            violations.put(entry, deps);
        }
        deps.add(illegalDependency);
    }

    public void add(SliceEntry entry,  Set<SliceEntry> illegalDependencies ) {
        Set<SliceEntry> deps = violations.get(entry);
        if (deps == null) {
            deps = new TreeSet<SliceEntry>();
            violations.put(entry, deps);
        }
        deps.addAll(illegalDependencies);
    }

    public Map<SliceEntry, Set<SliceEntry>> getViolations() {
        return violations;
    }

    public boolean isEmpty() {
        return violations.isEmpty();
    }
}
