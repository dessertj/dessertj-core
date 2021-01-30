package de.spricom.dessert.slicing;

import de.spricom.dessert.matching.NamePattern;
import de.spricom.dessert.resolve.TraversalRoot;
import de.spricom.dessert.util.Predicate;

import java.util.Set;

public abstract class AbstractRootSlice extends AbstractSlice {
    private final TraversalRoot traversalRoot;
    private ConcreteSlice concreteSlice;

    public AbstractRootSlice(TraversalRoot traversalRoot) {
        this.traversalRoot = traversalRoot;
    }

    public Slice packageOf(Class<?> clazz) {
        return packageOf(clazz.getPackage());
    }

    public Slice packageOf(Package pkg) {
        return packageOf(pkg.getName());
    }

    public Slice packageOf(String packageName) {
        return slice(packageName + ".*");
    }

    public Slice packageTreeOf(Class<?> clazz) {
        return packageTreeOf(clazz.getPackage());
    }

    public Slice packageTreeOf(Package pkg) {
        return packageTreeOf(pkg.getName());
    }

    public Slice packageTreeOf(String packageName) {
        return slice(packageName + "..*");
    }

    @Override
    public Slice combine(final Slice other) {
        if (concreteSlice != null) {
            return concreteSlice.combine(other);
        }
        return new DeferredSlice(other, resolver());
    }

    @Override
    public Slice slice(String pattern) {
        if (concreteSlice != null) {
            return concreteSlice.slice(pattern);
        }
        NamePattern namePattern = NamePattern.of(pattern);
        return new DeferredSlice(new DerivedSlice(namePattern), resolver(namePattern));
    }

    @Override
    public Slice slice(final Predicate<Clazz> predicate) {
        if (concreteSlice != null) {
            return concreteSlice.slice(predicate);
        }
        DerivedSlice derivedSlice = new DerivedSlice(predicate);
        return new DeferredSlice(derivedSlice, resolver());
    }

    @Override
    public boolean contains(Clazz clazz) {
        if (concreteSlice != null) {
            return concreteSlice.contains(clazz);
        }
        return slice(clazz.getName()).contains(clazz);
    }

    @Override
    public boolean isIterable() {
        return true;
    }

    @Override
    public Set<Clazz> getClazzes() {
        return getConcreteSlice().getClazzes();
    }

    private ConcreteSlice getConcreteSlice() {
        if (concreteSlice == null) {
            concreteSlice = new ConcreteSlice(resolver().getClazzes());
        }
        return concreteSlice;
    }

    private NameResolver resolver() {
        return resolver(NamePattern.ANY_NAME);
    }

    private NameResolver resolver(NamePattern namePattern) {
        return new NameResolver(getClasspath(), namePattern, traversalRoot);
    }

    abstract Classpath getClasspath();
}
