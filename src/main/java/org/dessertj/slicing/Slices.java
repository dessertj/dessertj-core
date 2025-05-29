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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Collection of utility methods for {@link Slice}.
 */
public final class Slices {
    /**
     * An empty slice.
     */
    public static final ConcreteSlice EMPTY_SLICE = ConcreteSlice.EMPTY_SLICE;

    private Slices() {
    }

    /**
     * Returns a new {@link Slice} that resembles the union of the <i>slices</i>.
     * Returns an empty slice if <i>slices</i> is empty.
     *
     * @param slices the slices to create the union from
     * @return the union of the slices
     */
    public static Slice of(Iterable<? extends Slice> slices) {
        List<Slice> list = new LinkedList<Slice>();
        for (Slice slice : slices) {
            list.add(slice);
        }
        if (list.isEmpty()) {
            return EMPTY_SLICE;
        } else if (list.size() == 1) {
            return list.get(0);
        }
        return new UnionSlice(list);
    }

    /**
     * Returns a new {@link Slice} that resembles the union of the <i>slices</i>.
     * Returns an empty slice if <i>slices</i> is empty.
     *
     * @param slices the slices to create the union from
     * @return the union of the slices
     */
    public static Slice of(Slice... slices) {
        if (slices.length == 0) {
            return EMPTY_SLICE;
        } else if (slices.length == 1) {
            return slices[0];
        }
        return new UnionSlice(Arrays.asList(slices));
    }
}
