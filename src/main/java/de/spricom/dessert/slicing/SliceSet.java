package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.ClassContainer;
import de.spricom.dessert.resolve.ClassPackage;
import de.spricom.dessert.resolve.ClassPredicate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A SliceSet is as Set of the slices for which the elements of each
 * {@link Slice} have common properties. I. e. they belong to the same parent
 * package, the same root, implement the same interface, comply with the same
 * naming convention etc.
 */
public class SliceSet implements Iterable<Slice> {
    private final Set<Slice> slices;

    SliceSet() {
        slices = new HashSet<Slice>();
    }

    SliceSet(int expectedSize) {
        slices = new HashSet<Slice>(expectedSize);
    }

    SliceSet(Slice slice) {
        slices = Collections.singleton(slice);
    }

    @Override
    public Iterator<Slice> iterator() {
        return slices.iterator();
    }

    public SliceSet with(SliceSet other) {
        SliceSet ss = new SliceSet(slices.size() + other.slices.size());
        ss.slices.addAll(slices);
        ss.slices.addAll(other.slices);
        return ss;
    }

    public SliceSet without(SliceSet other) {
        SliceSet ss = new SliceSet(slices.size() + other.slices.size());
        ss.slices.addAll(slices);
        ss.slices.removeAll(other.slices);
        return ss;
    }

    public SliceSet slice(ClassPredicate<SliceEntry> predicate) {
        SliceSet ss = new SliceSet(slices.size());
        for (Slice s : slices) {
            Slice filtered = s.slice(predicate);
            if (!filtered.getEntries().isEmpty()) {
                ss.slices.add(filtered);
            }
        }
        return ss;
    }

    void add(ClassContainer cc, SliceContext context) {
        if (cc == null || cc.getClasses() == null || cc.getClasses().isEmpty()) {
            return;
        }
        slices.add(new Slice(cc, context));
    }

    void addRecursive(ClassContainer cc, SliceContext context) {
        add(cc, context);
        for (ClassPackage subp : cc.getSubPackages()) {
            addRecursive(subp, context);
        }
    }
}
