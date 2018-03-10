package de.spricom.dessert.assertions;

import de.spricom.dessert.slicing.SliceEntry;

import java.util.*;

public class DefaultIllegalDependenciesRenderer implements IllegalDependenciesRenderer {
    @Override
    public String render(IllegalDependencies violations) {
        Map<SliceEntry, Set<SliceEntry>> dependecyViolations = violations.getViolations();
        StringBuilder sb = new StringBuilder("Illegal Dependencies:\n");
        for (SliceEntry entry : sort(dependecyViolations.keySet())) {
            sb.append(entry.getClassname()).append("\n");
            for (SliceEntry dep : dependecyViolations.get(entry)) {
                sb.append(" -> ").append(dep.getClassname()).append("\n");
            }
        }
        return sb.toString();
    }

    private Collection<SliceEntry> sort(Set<SliceEntry> entries) {
        TreeMap<String, SliceEntry> sorted = new TreeMap<String, SliceEntry>();
        for (SliceEntry entry : entries) {
            sorted.put(entry.getClassname(), entry);
        }
        return sorted.values();
    }
}
