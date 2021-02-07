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

    Slice plus(Iterable<? extends Slice> slices);

    Slice plus(Slice... slices);

    Slice minus(Iterable<? extends Slice> slices);

    Slice minus(Slice... slices);

    Slice combine(Slice other);

    Slice slice(String pattern);

    Slice slice(Predicate<Clazz> predicate);

    boolean contains(Clazz entry);

    boolean isIterable();

    Set<Clazz> getClazzes();

    ConcreteSlice getDependencies();

    /**
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
    Slice as(String name);
}
