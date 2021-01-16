package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.ClassRoot;
import de.spricom.dessert.util.Predicate;

import java.net.URI;
import java.util.Set;

/**
 * A special {@link Slice} that represents a whole JAR file, classes directory, module or other
 * single source of classes. The slice contains all its .class files.
 */
public class Root extends AbstractSlice {
    private final ClassRoot root;
    private final EntryResolver resolver;
    private ConcreteSlice concreteSlice;

    Root(ClassRoot root, EntryResolver resolver) {
        this.root = root;
        this.resolver = resolver;
    }

    @Override
    public Slice combine(final Slice other) {
        return getConcreteSlice().combine(other);
    }

    @Override
    public Slice slice(final Predicate<Clazz> predicate) {
        return getConcreteSlice().slice(predicate);
    }

    @Override
    public boolean contains(Clazz clazz) {
        return getConcreteSlice().contains(clazz);
    }

    @Override
    public boolean canResolveSliceEntries() {
        return true;
    }

    @Override
    public Set<Clazz> getSliceEntries() {
        return getConcreteSlice().getSliceEntries();
    }

    public URI getURI() {
        return root.getRootFile().toURI();
    }

    private ConcreteSlice getConcreteSlice() {
        if (concreteSlice == null) {
            concreteSlice = new ConcreteSlice(resolver.getSliceEntries());
        }
        return concreteSlice;
    }
}
