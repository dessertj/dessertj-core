package de.spricom.dessert.slicing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import de.spricom.dessert.dependency.ClassFileEntry;

/**
 * A SliceSet is as Set of the slices for which the elements of each {@link Slice} have common properties.
 * I. e. they belong to the same parent package, the same root, implement the same interface, comply with
 * the same naming convention etc.  
 */
public class SliceSet implements Iterable<Slice> {
    private List<Slice> slices = new ArrayList<>();

    @Override
    public Iterator<Slice> iterator() {
        return slices.iterator();
    }

    public SliceSet slice(Predicate<ClassFileEntry> predicate) {
        return new SliceSet();
    }
}
