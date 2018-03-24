package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;

import java.util.HashSet;
import java.util.Set;

public class DeferredSlice implements Slice {
    private final Predicate<SliceEntry> predicate;
    private final EntryResolver resolver;
    private final String description;
    private Set<SliceEntry> sliceEntries;
    private boolean derived;
    private boolean resolved;

    protected DeferredSlice(Set<SliceEntry> sliceEntries, String description) {
        assert sliceEntries != null : "sliceEntries == null";
        predicate = null;
        resolver = null;
        this.description = description;
        this.sliceEntries = sliceEntries;
        resolved = true;
    }

    DeferredSlice(Predicate<SliceEntry> predicate, EntryResolver resolver, String description) {
        this.predicate = predicate;
        this.resolver = resolver;
        this.description = description;
    }

    private DeferredSlice(Predicate<SliceEntry> predicate, DeferredSlice other, String description) {
        this.predicate = predicate;
        this.resolver = other.resolver;
        this.description = description;
        this.derived = true;
    }

    public String toString() {
        return description;
    }

    @Override
    public Slice with(final Slice other) {
        return new DeferredSlice(new Predicate<SliceEntry>() {
            @Override
            public boolean test(SliceEntry sliceEntry) {
                return contains(sliceEntry) || other.contains(sliceEntry);
            }
        }, this, "(" + this + ") with (" + other + ")");
    }

    @Override
    public Slice without(final Slice other) {
        return new DeferredSlice(new Predicate<SliceEntry>() {
            @Override
            public boolean test(SliceEntry sliceEntry) {
                return contains(sliceEntry) && !other.contains(sliceEntry);
            }
        }, this, "(" + this + ") without (" + other + ")");
    }

    @Override
    public Slice slice(final Predicate<SliceEntry> predicate) {
        return new DeferredSlice(new Predicate<SliceEntry>() {
            @Override
            public boolean test(SliceEntry sliceEntry) {
                return contains(sliceEntry) && predicate.test(sliceEntry);
            }
        }, this, "(" + this + ") filtered");
    }

    @Override
    public boolean contains(SliceEntry entry) {
        if (sliceEntries == null) {
            sliceEntries = new HashSet<SliceEntry>();
        } else if (sliceEntries.contains(entry)) {
            return true;
        }
        if (resolved) {
            return false;
        }
        boolean member = predicate.test(entry);
        if (member) {
            sliceEntries.add(entry);
        }
        return member;
    }

    @Override
    public Set<SliceEntry> getSliceEntries() {
        if (resolved) {
            return sliceEntries;
        }
        resolved = true;
        sliceEntries = new HashSet<SliceEntry>();
        for (SliceEntry entry : resolver.getSliceEntries()) {
            if (!derived || predicate.test(entry)) {
                sliceEntries.add(entry);
            }
        }
        return sliceEntries;
    }
}
