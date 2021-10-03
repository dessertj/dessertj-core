package de.spricom.dessert.modules;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
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

import de.spricom.dessert.slicing.Clazz;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.util.Predicate;

import java.util.LinkedList;
import java.util.List;

public class SliceDescriptionBuilder implements SliceDescription {
    private final List<SliceDescription> descriptions = new LinkedList<SliceDescription>();

    /**
     * Creates a new slices as union from this slice and the slices passed.
     *
     * @param slices the slices for the union
     * @return the union
     */
    public SliceDescriptionBuilder plus(final Iterable<? extends Slice> slices) {
        descriptions.add(new SliceDescription() {
            @Override
            public Slice apply(Slice slice) {
                return slice.plus(slices);
            }
        });
        return this;
    }

    /**
     * Creates a new slices as union from this slice and the slices passed.
     *
     * @param slices the slices for the union
     * @return the union
     */
    public SliceDescriptionBuilder plus(final Slice... slices) {
        descriptions.add(new SliceDescription() {
            @Override
            public Slice apply(Slice slice) {
                return slice.plus(slices);
            }
        });
        return this;
    }

    /**
     * Creates a new slice that resembles the difference of this slice and the
     * slices passed. Hence, the new slice contains all classes of this slice
     * that belongs to none of slices.
     *
     * @param slices the slices to create the difference from
     * @return the difference
     */
    public SliceDescriptionBuilder minus(final Iterable<? extends Slice> slices) {
        descriptions.add(new SliceDescription() {
            @Override
            public Slice apply(Slice slice) {
                return slice.minus(slices);
            }
        });
        return this;
    }

    /**
     * Creates a new slice that resembles the difference of this slice and the
     * slices passed.
     *
     * @param slices the slices to create the difference from
     * @return the difference
     */
    public SliceDescriptionBuilder minus(final Slice... slices) {
        descriptions.add(new SliceDescription() {
            @Override
            public Slice apply(Slice slice) {
                return slice.minus(slices);
            }
        });
        return this;
    }

    /**
     * This is a convenience method for {@code this.minus(this.slice(pattern))}.
     *
     * @param pattern the pattern for the slice to subtract from this slice
     * @return a new slice without classes matching pattern
     */
    public SliceDescriptionBuilder plus(final String pattern) {
        descriptions.add(new SliceDescription() {
            @Override
            public Slice apply(Slice slice) {
                return slice.minus(pattern);
            }
        });
        return this;
    }

    /**
     * This is a convenience method for {@code this.minus(this.slice(predicate))}.
     *
     * @param predicate the pattern for the slice to subtract from this slice
     * @return a new slice without classes fulfilling predicate
     */
    public SliceDescriptionBuilder minus(final Predicate<Clazz> predicate) {
        descriptions.add(new SliceDescription() {
            @Override
            public Slice apply(Slice slice) {
                return slice.minus(predicate);
            }
        });
        return this;
    }

    /**
     * Creates a new slice that resembles the intersection of this slice and union of the
     * slices passed. Hence, the new slice contains all classes of this slice
     * that belongs to any of slices.
     *
     * @param slices the slices to create the intersection from
     * @return the difference
     */
    public SliceDescriptionBuilder slice(final Iterable<? extends Slice> slices) {
        descriptions.add(new SliceDescription() {
            @Override
            public Slice apply(Slice slice) {
                return slice.slice(slices);
            }
        });
        return this;
    }

    /**
     * Creates a new slice that resembles the intersection of this slice and the
     * slices passed.
     *
     * @param slices the slices to create the intersection from
     * @return the difference
     */
    public SliceDescriptionBuilder slice(final Slice... slices) {
        descriptions.add(new SliceDescription() {
            @Override
            public Slice apply(Slice slice) {
                return slice.slice(slices);
            }
        });
        return this;
    }

    /**
     * Creates a new slices of all classes of this slide that match the specified
     * name pattern.
     *
     * @param pattern the pattern
     * @return the slice
     */
    public SliceDescriptionBuilder slice(final String pattern) {
        descriptions.add(new SliceDescription() {
            @Override
            public Slice apply(Slice slice) {
                return slice.slice(pattern);
            }
        });
        return this;
    }

    /**
     * Creates a new slices of all classes of this slide that fulfill the specified
     * name predicate.
     *
     * @param predicate the predicate
     * @return the slice
     * @see de.spricom.dessert.partitioning.ClazzPredicates for predefined predicates
     */
    public SliceDescriptionBuilder slice(final Predicate<Clazz> predicate) {
        descriptions.add(new SliceDescription() {
            @Override
            public Slice apply(Slice slice) {
                return slice.slice(predicate);
            }
        });
        return this;
    }

    @Override
    public Slice apply(Slice slice) {
        for (SliceDescription operation : descriptions) {
            slice = operation.apply(slice);
        }
        return slice;
    }
}
