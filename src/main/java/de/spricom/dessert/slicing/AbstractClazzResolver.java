package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.ClassEntry;

import java.util.HashSet;
import java.util.Set;

abstract class AbstractClazzResolver implements ClazzResolver {
    private final Classpath classpath;

    private Set<Clazz> clazzes;

    public AbstractClazzResolver(Classpath classpath) {
        this.classpath = classpath;
    }

    protected abstract void resolve();

    protected final void add(ClassEntry ce) {
        clazzes.add(classpath.asClazz(ce));
    }

    public Set<Clazz> getClazzes() {
        if (clazzes == null) {
            clazzes = new HashSet<Clazz>();
            resolve();
        }
        return clazzes;
    }

    public Classpath getClasspath() {
        return classpath;
    }
}
