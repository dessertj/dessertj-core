package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;

import java.util.Set;

public class DeferredSlice extends AbstractSlice {
    private final Slice derivedSlice;
    private final EntryResolver resolver;
    private ConcreteSlice concreteSlice;

    DeferredSlice(Slice derivedSlice, EntryResolver resolver) {
        this.derivedSlice = derivedSlice;
        this.resolver = resolver;
    }

    @Override
    public Slice combine(final Slice other) {
        if (concreteSlice != null) {
            return concreteSlice.combine(other);
        }
        return new DeferredSlice(derivedSlice.combine(other), resolver);
    }

    @Override
    public Slice slice(final Predicate<Clazz> predicate) {
        if (concreteSlice != null) {
            return concreteSlice.slice(predicate);
        }
        return new DeferredSlice(derivedSlice.slice(predicate), resolver);
    }

    @Override
    public boolean contains(Clazz entry) {
        if (concreteSlice != null) {
            return concreteSlice.contains(entry);
        }
        return derivedSlice.contains(entry);
    }

    @Override
    public boolean canResolveSliceEntries() {
        return true;
    }

    @Override
    public Set<Clazz> getSliceEntries() {
        if (concreteSlice == null) {
            ConcreteSlice cs = new ConcreteSlice(resolver.getSliceEntries());
            concreteSlice = cs.slice(new Predicate<Clazz>() {
                @Override
                public boolean test(Clazz clazz) {
                    return derivedSlice.contains(clazz);
                }
            });
        }
        return concreteSlice.getSliceEntries();
    }
}
