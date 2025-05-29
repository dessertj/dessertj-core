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

import org.dessertj.util.Predicate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class UnionSlice extends AbstractSlice {
    private final List<Slice> slices;

    public UnionSlice(List<Slice> slices) {
        if (slices.size() < 2) {
            throw new IllegalArgumentException("Union must contain at least two slices");
        }
        this.slices = slices;
    }

    @Override
    public Slice slice(Predicate<Clazz> predicate) {
        List<Slice> derived = new ArrayList<Slice>(slices.size());
        for (Slice slice : slices) {
            derived.add(slice.slice(predicate));
        }
        return new UnionSlice(derived);
    }

    @Override
    public boolean contains(Clazz clazz) {
        for (Slice slice : slices) {
            if (slice.contains(clazz)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Clazz> getClazzes() {
        // TODO: Could be optimized by implementing a UnionSet.
        HashSet<Clazz> clazzes = new HashSet<Clazz>();
        for (Slice slice : slices) {
            clazzes.addAll(slice.getClazzes());
        }
        return clazzes;
    }
}
