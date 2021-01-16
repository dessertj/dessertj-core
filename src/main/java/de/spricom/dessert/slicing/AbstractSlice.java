package de.spricom.dessert.slicing;

import de.spricom.dessert.util.Predicate;
import de.spricom.dessert.util.SetHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Defines convenience methods available to all {@link Slice} implementations.
 */
public abstract class AbstractSlice implements Slice {

    public Slice plus(Slice... slices) {
        Slice sum = this;
        for (Slice slice : slices) {
            sum = sum.combine(slice);
        }
        return sum;
    }

    public Slice minus(Slice... slices) {
        if (slices.length == 0) {
            return this;
        }
        final Slice union = Slices.of(slices);
        Predicate<Clazz> excluded = new Predicate<Clazz>() {

            @Override
            public boolean test(Clazz clazz) {
                return !union.contains(clazz);
            }
        };
        return slice(excluded);
    }

}
