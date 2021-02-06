package de.spricom.dessert.slicing;

import de.spricom.dessert.matching.NamePattern;
import de.spricom.dessert.util.Predicate;

import java.util.*;

/**
 * Defines convenience methods available to all {@link Slice} implementations.
 */
public abstract class AbstractSlice implements Slice {

    public Slice plus(Iterable<? extends Slice> slices) {
        Slice sum = this;
        for (Slice slice : slices) {
            sum = sum.combine(slice);
        }
        return sum;
    }

    public Slice plus(Slice... slices) {
        return plus(Arrays.asList(slices));
    }

    public Slice minus(Iterable<? extends Slice> slices) {
        final Slice union = Slices.of(slices);
        Predicate<Clazz> excluded = new Predicate<Clazz>() {

            @Override
            public boolean test(Clazz clazz) {
                return !union.contains(clazz);
            }
        };
        return slice(excluded);
    }

    public Slice minus(Slice... slices) {
        return minus(Arrays.asList(slices));
    }

    @Override
    public Slice slice(String pattern) {
        final NamePattern namePattern = NamePattern.of(pattern);
        return slice(new Predicate<Clazz>() {

            @Override
            public boolean test(Clazz clazz) {
                return namePattern.matches(clazz.getName());
            }
        });
    }

    @Override
    public ConcreteSlice getDependencies() {
        Set<Clazz> dependencies = new HashSet<Clazz>();
        for (Clazz clazz : getClazzes()) {
            dependencies.addAll(clazz.getDependencies().getClazzes());
        }
        return new ConcreteSlice(dependencies);
    }

    @Override
    public boolean uses(Slice other) {
        if (this == other) {
            return false;
        }
        for (Clazz clazz : getClazzes()) {
            for (Clazz dependency : clazz.getDependencies().getClazzes()) {
                for (Clazz alternative : dependency.getAlternatives()) {
                    if (other.contains(alternative)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Slice as(String name) {
        return new NamedSlice(this, name);
    }

    public SortedMap<String, PackageSlice> partitionByPackage() {
        return partitionBy(PackageSlice.partitioner(), PackageSlice.factory());
    }

    public SortedMap<String, PartitionSlice> partitionBy(SlicePartitioner partitioner) {
        return partitionBy(
                partitioner,
                new PartitionSliceFactory<PartitionSlice>() {
                    @Override
                    public PartitionSlice createPartSlice(String partKey, Set<Clazz> entries, Map<String, PartitionSlice> slices) {
                        return new PartitionSlice(partKey, entries);
                    }
                });
    }

    public <S extends PartitionSlice> SortedMap<String, S> partitionBy(SlicePartitioner partioner, PartitionSliceFactory<S> partitionSliceFactory) {
        Map<String, Set<Clazz>> split = new HashMap<String, Set<Clazz>>();
        for (Clazz entry : getClazzes()) {
            String key = partioner.partKey(entry);
            if (key != null) {
                Set<Clazz> matches = split.get(key);
                if (matches == null) {
                    matches = new HashSet<Clazz>();
                    split.put(key, matches);
                }
                matches.add(entry);
            }
        }
        SortedMap<String, S> slices = new TreeMap<String, S>();
        for (Map.Entry<String, Set<Clazz>> matches : split.entrySet()) {
            slices.put(matches.getKey(), partitionSliceFactory.createPartSlice(matches.getKey(), matches.getValue(), slices));
        }
        return slices;
    }
}
