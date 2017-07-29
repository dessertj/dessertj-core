package de.spricom.dessert.dependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DependencyUnit {
    private final Map<String, ClassFileEntry> members;
    private Map<String, ClassFileEntry> directDependencies;
    private Map<String, ClassFileEntry> transitiveDependencies;
    private String name;

    public DependencyUnit(Map<String, ClassFileEntry> members) {
        this.members = members;
    }

    public boolean isUnique() {
        for (ClassFileEntry entry : members.values()) {
            if (!entry.isUnique()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean hasUndefinedDependencies() {
        return getDirectDependencies().containsValue(null);
    }

    public boolean isContainedIn(DependencyUnit dependencyUnit) {
        return dependencyUnit.members.keySet().containsAll(members.keySet());
    }

    public boolean dependsDirectlyOn(DependencyUnit dependencyUnit) {
        return !isDistinct(getDirectDependencies().keySet(), dependencyUnit.members.keySet());
    }
    
    public boolean isIndependentFrom(DependencyUnit dependencyUnit) {
        return isDistinct(getTransitiveDependencies().keySet(), dependencyUnit.getMembers().keySet());
    }
    
    public boolean isTransitiveDistinct(DependencyUnit dependencyUnit, DependencyUnit... excluding) {
        List<Set<String>> excludingList = new ArrayList<>(excluding.length);
        for (DependencyUnit excludedUnit : excluding) {
            excludingList.add(excludedUnit.getMembers().keySet());
        }
        return isDistinct(getTransitiveDependencies().keySet(), dependencyUnit.getTransitiveDependencies().keySet(), excludingList);
    }
    
    public Map<String, ClassFileEntry> getMembers() {
        return members;
    }

    public Map<String, ClassFileEntry> getDirectDependencies() {
        if (directDependencies == null) {
            directDependencies = calculateDirectDependencies();
        }
        return directDependencies;
    }
    
    public Set<String> getUndefinedDirectDependencies() {
        return filterUndefined(getDirectDependencies());
    }

    public Map<String, ClassFileEntry> getTransitiveDependencies() {
        if (transitiveDependencies == null) {
            transitiveDependencies = calculateTransitiveDependencies();
        }
        return transitiveDependencies;
    }

    public Set<String> getUndefinedTransitiveDependencies() {
        return filterUndefined(getTransitiveDependencies());
    }

    private Set<String> filterUndefined(Map<String, ClassFileEntry> dependencies) {
        Set<String> set = new HashSet<>();
        for (Map.Entry<String, ClassFileEntry> entry : dependencies.entrySet()) {
            if (entry.getValue() == null) {
                set.add(entry.getKey());
            }
        }
        return set;
    }
    
    private Map<String, ClassFileEntry> calculateDirectDependencies() {
        Map<String, ClassFileEntry> dependencies = new HashMap<>(members);
        for (ClassFileEntry member : members.values()) {
            for (String classname : member.getDependentClassNames()) {
                dependencies.put(classname, member.lookup(classname));
            }
        }
        return dependencies;
    }

    private Map<String, ClassFileEntry> calculateTransitiveDependencies() {
        Map<String, ClassFileEntry> done = new HashMap<>(members);
        Map<String, ClassFileEntry> todo = new HashMap<>(getDirectDependencies());
        while (!todo.isEmpty()) {
            String classname = todo.keySet().iterator().next();
            ClassFileEntry entry = todo.remove(classname);
            if (entry != null) {
                for (String name : entry.getDependentClassNames()) {
                    if (!done.containsKey(name) && !todo.containsKey(name)) {
                        todo.put(name, entry.lookup(name));
                    }
                }
            }
        }
        return done;
    }

    private static boolean isDistinct(Set<String> p, Set<String> q) {
        return isDistinct(p, q, Collections.emptyList());
    }
    
    private static boolean isDistinct(Set<String> p, Set<String> q, List<Set<String>> excluding) {
        if (p.size() > q.size()) {
            Set<String> a = p;
            p = q;
            q = a;
        }
        for (String s : p) {
            if (q.contains(s) && isExcluded(s, excluding)) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean isExcluded(String s, List<Set<String>> excluding) {
        for (Set<String> set : excluding) {
            if (set.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DependencyUnit [name=" + name + ", size=" + members.size() + "]";
    }
}
