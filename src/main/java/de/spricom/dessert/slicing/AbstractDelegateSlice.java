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

import de.spricom.dessert.util.Predicate;

import java.util.Set;
import java.util.SortedMap;

public abstract class AbstractDelegateSlice implements Slice {

    protected abstract Slice getDelegate();

    @Override
    public Slice plus(Iterable<? extends Slice> slices) {
        return getDelegate().plus(slices);
    }

    @Override
    public Slice plus(Slice... slices) {
        return getDelegate().plus(slices);
    }

    @Override
    public Slice minus(Iterable<? extends Slice> slices) {
        return getDelegate().minus(slices);
    }

    @Override
    public Slice minus(Slice... slices) {
        return getDelegate().minus(slices);
    }

    @Override
    public Slice minus(String pattern) {
        return getDelegate().slice(pattern);
    }

    @Override
    public Slice minus(Predicate<Clazz> predicate) {
        return getDelegate().slice(predicate);
    }

    @Override
    public Slice slice(Iterable<? extends Slice> slices) {
        return getDelegate().slice(slices);
    }

    @Override
    public Slice slice(Slice... slices) {
        return getDelegate().slice(slices);
    }

    @Override
    public Slice slice(String pattern) {
        return getDelegate().slice(pattern);
    }

    @Override
    public Slice slice(Predicate<Clazz> predicate) {
        return getDelegate().slice(predicate);
    }

    @Override
    public boolean contains(Clazz entry) {
        return getDelegate().contains(entry);
    }

    @Override
    public Set<Clazz> getClazzes() {
        return getDelegate().getClazzes();
    }

    @Override
    public ConcreteSlice getDependencies() {
        return getDelegate().getDependencies();
    }

    @Override
    public boolean uses(Slice other) {
        return getDelegate().uses(other);
    }

    @Override
    public SortedMap<String, PackageSlice> partitionByPackage() {
        return getDelegate().partitionByPackage();
    }

    @Override
    public SortedMap<String, PartitionSlice> partitionBy(SlicePartitioner partitioner) {
        return getDelegate().partitionBy(partitioner);
    }

    @Override
    public <S extends PartitionSlice> SortedMap<String, S> partitionBy(SlicePartitioner partitioner, PartitionSliceFactory<S> partitionSliceFactory) {
        return getDelegate().partitionBy(partitioner, partitionSliceFactory);
    }
}
