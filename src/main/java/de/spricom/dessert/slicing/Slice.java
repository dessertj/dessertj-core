package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;

import java.util.Set;
import java.util.SortedMap;

/**
 * A Slice is an arbitrary Set of {@link Clazz}.
 * A Slice is immutable in behaviour. It may use deferred evaluation or caching, but each method (incl. toString())
 * must produce the same result in any circumstance.
 * Two different Slice instances are never equal, even if they represent the same set of classes.
 */
public interface Slice {

    Slice plus(Iterable<? extends Slice> slices);

    Slice plus(Slice... slices);

    Slice minus(Iterable<? extends Slice> slices);

    Slice minus(Slice... slices);

    Slice combine(Slice other);

    Slice slice(Predicate<Clazz> predicate);

    boolean contains(Clazz entry);

    boolean canResolveSliceEntries();

    Set<Clazz> getSliceEntries();

    SortedMap<String, PackageSlice> splitByPackage();

    SortedMap<String, PartSlice> splitBy(SlicePartitioner partitioner);

    <S extends PartSlice> SortedMap<String, S> splitBy(SlicePartitioner partitioner, PartSliceFactory<S> partSliceFactory);

    /**
     * Creates a new {@link Slice} from this slice for which the
     * {@link #toString()} returns <i>name</i>.
     *
     * @param name the name of this slice
     * @return a new slice with that name
     */
    Slice as(String name);
}
