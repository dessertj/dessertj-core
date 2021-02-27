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

public abstract class AbstractDelegateSlice implements Slice {

    private final Slice delegate;

    AbstractDelegateSlice(Slice delegate) {
        this.delegate = delegate;
    }

    public Slice getDelegate() {
        return delegate;
    }

    @Override
    public Slice plus(Iterable<? extends Slice> slices) {
        return delegate.plus(slices);
    }

    @Override
    public Slice plus(Slice... slices) {
        return delegate.plus(slices);
    }

    @Override
    public Slice minus(Iterable<? extends Slice> slices) {
        return delegate.minus(slices);
    }

    @Override
    public Slice minus(Slice... slices) {
        return delegate.minus(slices);
    }

    @Override
    public Slice minus(String pattern) {
        return delegate.slice(pattern);
    }

    @Override
    public Slice minus(Predicate<Clazz> predicate) {
        return delegate.slice(predicate);
    }

    @Override
    public Slice slice(Iterable<? extends Slice> slices) {
        return delegate.slice(slices);
    }

    @Override
    public Slice slice(Slice... slices) {
        return delegate.slice(slices);
    }

    @Override
    public Slice slice(String pattern) {
        return delegate.slice(pattern);
    }

    @Override
    public Slice slice(Predicate<Clazz> predicate) {
        return delegate.slice(predicate);
    }

    @Override
    public boolean contains(Clazz entry) {
        return delegate.contains(entry);
    }

    @Override
    public Set<Clazz> getClazzes() {
        return delegate.getClazzes();
    }

    @Override
    public ConcreteSlice getDependencies() {
        return delegate.getDependencies();
    }

    @Override
    public boolean uses(Slice other) {
        return delegate.uses(other);
    }

    @Override
    public SortedMap<String, PackageSlice> partitionByPackage() {
        return delegate.partitionByPackage();
    }

    @Override
    public SortedMap<String, PartitionSlice> partitionBy(SlicePartitioner partitioner) {
        return delegate.partitionBy(partitioner);
    }

    @Override
    public <S extends PartitionSlice> SortedMap<String, S> partitionBy(SlicePartitioner partitioner, PartitionSliceFactory<S> partitionSliceFactory) {
        return delegate.partitionBy(partitioner, partitionSliceFactory);
    }
}
