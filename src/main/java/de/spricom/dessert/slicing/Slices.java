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

import java.util.Arrays;

/**
 * Factory methods for {@link Slice}.
 */
public final class Slices {
    public static final ConcreteSlice EMPTY_SLICE = ConcreteSlice.EMPTY_SLICE;

    private Slices() {
    }

    /**
     * Returns a new {@link Slice} that resembels the union of the <i>slices</i>.
     * Returns an empty slice if <i>slices</i> is empty.
     *
     * @param slices the slices to create the union from
     * @return the union of the the slices
     */
    public static Slice of(Iterable<? extends Slice> slices) {
        return EMPTY_SLICE.plus(slices);
    }

    public static Slice of(Slice... slices) {
        return of(Arrays.asList(slices));
    }
}
