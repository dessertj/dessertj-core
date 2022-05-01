package de.spricom.dessert.slicing;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2022 Hans Jörg Heßmann
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

import de.spricom.dessert.resolve.ResolveException;
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
     * Creates a new slices of all classes of this slide that match the specified
     * name pattern.
     *
     * @param pattern the pattern
     * @return the slice
     */
    Slice slice(String pattern);

    /**
     * Creates a new slices of all classes of this slide that fulfill the specified
     * name predicate.
     *
     * @param predicate the predicate
     * @return the slice
     * @see de.spricom.dessert.partitioning.ClazzPredicates for predefined predicates
     */
    Slice slice(Predicate<Clazz> predicate);

    /**
     * Calculates the transitive closure of this slice's classes and their dependencies
     * intersected with the <i>within</i> slice.
     *
     * @param within the slice within which the transitive closure is calculated
     * @return the transitive closure of this slice and it's dependencies
     */
    Slice dependencyClosure(Slice within);

    /**
     * Check whether a class belongs to a slice.
     *
     * @param clazz the Clazz
     * @return true if the clazz belongs to this slice
     */
    boolean contains(Clazz clazz);

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
     * Returns a slice of all the dependencies of this slice. That is the union of all
     * the dependencies of each class that belongs to this slice.
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

    /**
     * Partitions the slice by package.
     *
     * @return a map of the packages by package name.
     */
    SortedMap<String, PackageSlice> partitionByPackage();

    /**
     * Partitions the slice by some {@link SlicePartitioner} that maps classes to names.
     * Clazzes the partitioner does not map to a name will be omitted form the result.
     *
     * @param partitioner the {@link SlicePartitioner} to partition by
     * @return a map of slices by partition name
     * @see de.spricom.dessert.partitioning.SlicePartitioners for predefined partitioners
     */
    SortedMap<String, PartitionSlice> partitionBy(SlicePartitioner partitioner);

    /**
     * Same as {@link #partitionBy(SlicePartitioner)}, but creates specialized subclasses
     * of {@link PartitionSlice} using a {@link PartitionSliceFactory}.
     *
     *
     * @param partitioner the {@link SlicePartitioner} to partition by
     * @param partitionSliceFactory the factory to create the slices
     * @param <S> the type of slices returned
     * @return a map of specialized slices by partition name
     */
    <S extends PartitionSlice> SortedMap<String, S> partitionBy(SlicePartitioner partitioner,
                                                                PartitionSliceFactory<S> partitionSliceFactory);

    /**
     * Creates a new {@link Slice} from this slice for which the
     * {@link Object#toString()} returns <i>name</i>.
     *
     * @param name the name of this slice
     * @return a new slice with that name
     */
    Slice named(String name);
}
