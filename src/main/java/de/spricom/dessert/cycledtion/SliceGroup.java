package de.spricom.dessert.cycledtion;

import de.spricom.dessert.slicing.Slice;

import java.util.Iterator;
import java.util.Set;

/**
 * A Slice is as Set of the slices for which the elements of each
 * {@link PackageSlice} have common properties. I. e. they belong to the same parent
 * package, the same root, implement the same interface, comply with the same
 * naming convention etc.
 */
public final class SliceGroup implements Iterable<Slice> {
    private Set<Slice> slices;

    @Override
    public Iterator<Slice> iterator() {
        return slices.iterator();
    }

}
