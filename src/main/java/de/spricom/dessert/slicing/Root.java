package de.spricom.dessert.slicing;

import de.spricom.dessert.matching.NamePattern;
import de.spricom.dessert.resolve.ClassRoot;
import de.spricom.dessert.util.Predicate;

import java.net.URI;
import java.util.Set;

/**
 * A special {@link Slice} that represents a whole JAR file, classes directory, module or other
 * single source of classes. The slice contains all its .class files.
 */
public class Root extends AbstractRootSlice {
    private final ClassRoot root;
    private final Classpath classpath;
    private ConcreteSlice concreteSlice;

    Root(ClassRoot root, Classpath classpath) {
        this.root = root;
        this.classpath = classpath;
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
        NameResolver nameResolver = new NameResolver(classpath, namePattern, root);
        DerivedSlice derivedSlice = new DerivedSlice(namePattern);
        return new DeferredSlice(derivedSlice, nameResolver);
    }

    @Override
    public Slice slice(final Predicate<Clazz> predicate) {
        if (concreteSlice != null) {
            return concreteSlice.slice(predicate);
        }
        NameResolver nameResolver = new NameResolver(classpath, NamePattern.ANY_NAME, root);
        DerivedSlice derivedSlice = new DerivedSlice(predicate);
        return new DeferredSlice(derivedSlice, nameResolver);
    }

    @Override
    public boolean contains(Clazz clazz) {
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

    public URI getURI() {
        return root.getRootFile().toURI();
    }

    ClassRoot getClassRoot() {
        return root;
    }

    private ConcreteSlice getConcreteSlice() {
        if (concreteSlice == null) {
            concreteSlice = new ConcreteSlice(resolver().getClazzes());
        }
        return concreteSlice;
    }

    private NameResolver resolver() {
        return new NameResolver(classpath, NamePattern.ANY_NAME, root);
    }

    public String toString() {
        return "root of " + root.getRootFile().getName();
    }
}
