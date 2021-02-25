package de.spricom.dessert.slicing;

/*-
 * #%L
 * Dessert Dependency Assertion Library
 * %%
 * Copyright (C) 2017 - 2021 Hans Jörg Heßmann
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

    /**
     * Creates a new slices as union from this slice and the slices passed.
     *
     * @param slices the slices for the union
     * @return the union
     */
    Slice plus(Iterable<? extends Slice> slices);

    /**
     * Creates a new slices as union from this slice and the slices passed.
     *
     * @param slices the slices for the union
     * @return the union
     */
    Slice plus(Slice... slices);

    /**
     * Creates a new slice that resembles the difference of this slice and the
     * slices passed. Hence, the new slice contains all classes of this slice
     * that belongs to none of slices.
     *
     * @param slices the slices to create the difference from
     * @return the difference
     */
    Slice minus(Iterable<? extends Slice> slices);

    /**
     * Creates a new slice that resembles the difference of this slice and the
     * slices passed.
     *
     * @param slices the slices to create the difference from
     * @return the difference
     */
    Slice minus(Slice... slices);

    /**
     * This is a convenience method for {@code this.minus(this.slice(pattern))}.
     *
     * @param pattern the pattern for the slice to subtract from this slice
     * @return a new slice without classes matching pattern
     */
    Slice minus(String pattern);

    /**
     * This is a convenience method for {@code this.minus(this.slice(predicate))}.
     *
     * @param predicate the pattern for the slice to subtract from this slice
     * @return a new slice without classes fulfilling predicate
     */
    Slice minus(Predicate<Clazz> predicate);

    /**
     * Creates a new slice that resembles the intersection of this slice and union of the
     * slices passed. Hence, the new slice contains all classes of this slice
     * that belongs to any of slices.
     *
     * @param slices the slices to create the intersection from
     * @return the difference
     */
    Slice slice(Iterable<? extends Slice> slices);

    /**
     * Creates a new slice that resembles the intersection of this slice and the
     * slices passed.
     *
     * @param slices the slices to create the intersection from
     * @return the difference
     */
    Slice slice(Slice... slices);

    /**
     *
     *
     * @param pattern
     * @return
     */
    Slice slice(String pattern);

    Slice slice(Predicate<Clazz> predicate);

    boolean contains(Clazz entry);

    /**
     * Returns all classes belonging to this slice. This is only possible for slices
     * based on the {@link Classpath}. A slice derived from some pattern or {@link Predicate}
     * will throw a {@link ResolveException} instead.
     *
     * @return a set of all classes belonging to this slice
     * @throws ResolveException if the classes cannot be determined
     */
    Set<Clazz> getClazzes();

    /**
     * Returns a slice of all the dependencies of this slices. That is the union of all
     * of the dependencies of each class that belongs to this slice.
     *
     * @return the dependencies
     */
    ConcreteSlice getDependencies();

    /**
     * Check whether there is a dependency to some other slice.
     *
     * @param other the other slice
     * @return true if this != other and there is one class in this slice that has a dependency contained in other
     */
    boolean uses(Slice other);

    SortedMap<String, PackageSlice> partitionByPackage();

    SortedMap<String, PartitionSlice> partitionBy(SlicePartitioner partitioner);

    <S extends PartitionSlice> SortedMap<String, S> partitionBy(SlicePartitioner partitioner, PartitionSliceFactory<S> partitionSliceFactory);

    /**
     * Creates a new {@link Slice} from this slice for which the
     * {@link Object#toString()} returns <i>name</i>.
     *
     * @param name the name of this slice
     * @return a new slice with that name
     */
    Slice named(String name);
}
