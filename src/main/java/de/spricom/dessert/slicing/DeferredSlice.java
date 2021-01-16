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
    public Slice with(final Slice other) {
        if (concreteSlice != null) {
            return concreteSlice.with(other);
        }
        return new DeferredSlice(derivedSlice.with(other), resolver);
    }

    @Override
    public Slice without(final Slice other) {
        if (concreteSlice != null) {
            return concreteSlice.without(other);
        }
        return new DeferredSlice(derivedSlice.without(other), resolver);
    }

    @Override
    public Slice slice(final Predicate<SliceEntry> predicate) {
        if (concreteSlice != null) {
            return concreteSlice.slice(predicate);
        }
        return new DeferredSlice(derivedSlice.slice(predicate), resolver);
    }

    @Override
    public boolean contains(SliceEntry entry) {
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
    public Set<SliceEntry> getSliceEntries() {
        if (concreteSlice == null) {
            ConcreteSlice cs = new ConcreteSlice(resolver.getSliceEntries());
            concreteSlice = cs.slice(new Predicate<SliceEntry>() {
                @Override
                public boolean test(SliceEntry sliceEntry) {
                    return derivedSlice.contains(sliceEntry);
                }
            });
        }
        return concreteSlice.getSliceEntries();
    }
}
