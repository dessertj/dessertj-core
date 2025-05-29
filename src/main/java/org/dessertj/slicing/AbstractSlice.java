package org.dessertj.slicing;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2025 Hans Jörg Heßmann
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

import org.dessertj.matching.NamePattern;
import org.dessertj.util.Predicate;

import java.util.*;

/**
 * Defines convenience methods available to all {@link Slice} implementations.
 */
public abstract class AbstractSlice implements Slice {

    AbstractSlice() {
    }

    @Override
    public Slice plus(Iterable<? extends Slice> slices) {
        List<Slice> list = new LinkedList<Slice>();
        list.add(this);
        for (Slice slice : slices) {
            list.add(slice);
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        return new UnionSlice(list);
    }

    @Override
    public Slice plus(Slice... slices) {
        if (slices.length == 0) {
            return this;
        }
        List<Slice> list = new ArrayList<Slice>(slices.length + 1);
        list.add(this);
        list.addAll(Arrays.asList(slices));
        return new UnionSlice(list);
    }

    @Override
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

    @Override
    public Slice minus(Slice... slices) {
        return minus(Arrays.asList(slices));
    }

    @Override
    public Slice minus(String pattern) {
        return this.minus(this.slice(pattern));
    }

    @Override
    public Slice minus(Predicate<Clazz> predicate) {
        return this.minus(this.slice(predicate));
    }

    @Override
    public Slice slice(Iterable<? extends Slice> slices) {
        List<Slice> list = new LinkedList<Slice>();
        for (Slice slice : slices) {
            list.add(slice);
        }
        if (list.isEmpty()) {
            return this;
        }
        final Slice union = list.size() == 1 ? list.get(0) : new UnionSlice(list);
        Predicate<Clazz> isInUnion = new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz clazz) {
                return union.contains(clazz);
            }
        };
        return slice(isInUnion);
    }

    @Override
    public Slice slice(Slice... slices) {
        return slice(Arrays.asList(slices));
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
    public Slice dependencyClosure(Slice within) {
        Slice result = this.slice(within);
        int count;
        do {
            count = result.getClazzes().size();
            result = result.plus(result.getDependencies()).slice(within);
        } while (result.getClazzes().size() != count);
        return result;
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
    public Slice named(String name) {
        return new NamedSlice(this, name);
    }

    @Override
    public SortedMap<String, PackageSlice> partitionByPackage() {
        return partitionBy(PackageSlice.partitioner(), PackageSlice.factory());
    }

    @Override
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

    @Override
    public <S extends PartitionSlice> SortedMap<String, S> partitionBy(SlicePartitioner partitioner, PartitionSliceFactory<S> partitionSliceFactory) {
        Map<String, Set<Clazz>> split = new HashMap<String, Set<Clazz>>();
        for (Clazz clazz : getClazzes()) {
            String key = partitioner.partKey(clazz);
            if (key != null) {
                Set<Clazz> matches = split.get(key);
                if (matches == null) {
                    matches = new HashSet<Clazz>();
                    split.put(key, matches);
                }
                matches.add(clazz);
            }
        }
        SortedMap<String, S> slices = new TreeMap<String, S>();
        for (Map.Entry<String, Set<Clazz>> matches : split.entrySet()) {
            slices.put(matches.getKey(), partitionSliceFactory.createPartSlice(matches.getKey(), matches.getValue(), slices));
        }
        return slices;
    }
}
