package de.spricom.dessert.assertions;

import de.spricom.dessert.slicing.Slice;

import java.util.Arrays;
import java.util.Map;

public final class SliceAssertions {
    private SliceAssertions() {
    }

    public static SliceAssert assertThat(Iterable<? extends Slice> slices) {
        return new SliceAssert(slices);
    }

    public static SliceAssert assertThat(Slice... slices) {
        return assertThat(Arrays.asList(slices));
    }

    public static SliceAssert assertThat(Map<String, ? extends Slice> slices) {
        return assertThat(slices.values());
    }

    public static SliceAssert dessert(Iterable<? extends Slice> slices) {
        return assertThat(slices);
    }

    public static SliceAssert dessert(Slice... slices) {
        return assertThat(slices);
    }

    public static SliceAssert dessert(Map<String, ? extends Slice> slices) {
        return assertThat(slices);
    }
}
