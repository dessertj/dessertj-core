package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;

import java.util.Set;
import java.util.SortedMap;

final class NamedSlice implements Slice {

    private final Slice delegate;
    private final String name;

    NamedSlice(Slice delegate, String name) {
        this.delegate = delegate;
        this.name = name;
    }

    public Slice as(String name) {
        return new NamedSlice(delegate, name);
    }

    public String toString() {
        return getName();
    }

    public Slice getDelegate() {
        return delegate;
    }

    public String getName() {
        return name;
    }

    /// delegate methods

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
    public Slice combine(Slice other) {
        return delegate.combine(other);
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
    public boolean isIterable() {
        return delegate.isIterable();
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
