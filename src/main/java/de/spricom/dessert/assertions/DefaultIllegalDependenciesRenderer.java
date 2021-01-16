package de.spricom.dessert.assertions;

import de.spricom.dessert.slicing.Clazz;

import java.util.*;

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
