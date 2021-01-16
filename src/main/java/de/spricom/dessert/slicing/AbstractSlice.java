package de.spricom.dessert.slicing;

import java.util.Arrays;

/**
 * Defines convenience methods available to all {@link Slice} implementations.
 */
public abstract class AbstractSlice implements Slice {

    public Slice plus(Slice... slices) {
        return this.with(Slices.of(slices));
    }

    public Slice minus(Slice... slices) {
        return this.without(Slices.of(slices));
    }
}
