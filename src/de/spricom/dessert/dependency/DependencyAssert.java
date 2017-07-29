package de.spricom.dessert.dependency;

import java.util.HashSet;
import java.util.Set;

public class DependencyAssert {
    private final Repository repository;
    private final DependencyUnit unit;
    
    public DependencyAssert(Repository repository, DependencyUnit unit) {
        this.repository = repository;
        this.unit = unit;
    }

    public DependencyAssert doesNotDependOn(Rule rule) {
        return doesNotDependOn(repository.getDependencyUnit(rule));
    }

    public DependencyAssert doesNotDependOn(DependencyUnit dependencyUnit) {
        if (!unit.isIndependentFrom(dependencyUnit)) {
            Set<String> violations = new HashSet<>(dependencyUnit.getMembers().keySet());
            violations.retainAll(unit.getTransitiveDependencies().keySet());
            throw new AssertionError(unit.getName() + " depends on " + dependencyUnit + ", because it needs " + violations);
        }
        return this;
    }
}
