package de.spricom.dessert.assertions;

import de.spricom.dessert.slicing.Clazz;

import java.util.*;

public class DefaultIllegalDependenciesRenderer implements IllegalDependenciesRenderer {
    @Override
    public String render(IllegalDependencies violations) {
        Map<Clazz, Set<Clazz>> dependecyViolations = violations.getViolations();
        StringBuilder sb = new StringBuilder("Illegal Dependencies:\n");
        for (Clazz entry : sort(dependecyViolations.keySet())) {
            sb.append(entry.getClassName()).append("\n");
            for (Clazz dep : dependecyViolations.get(entry)) {
                sb.append(" -> ").append(dep.getClassName()).append("\n");
            }
        }
        return sb.toString();
    }

    private Collection<Clazz> sort(Set<Clazz> entries) {
        TreeMap<String, Clazz> sorted = new TreeMap<String, Clazz>();
        for (Clazz entry : entries) {
            sorted.put(entry.getClassName(), entry);
        }
        return sorted.values();
    }
}
