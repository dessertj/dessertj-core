package de.spricom.dessert.assertions;

import de.spricom.dessert.groups.PartSlice;
import de.spricom.dessert.groups.SliceGroup;
import de.spricom.dessert.slicing.Slice;

public final class SliceAssertions {
    private SliceAssertions() {
    }

    public static SliceAssert assertThat(Slice slice) {
        return new SliceAssert(slice);
    }

    public static SliceAssert dessert(Slice slice) {
        return assertThat(slice);
    }

    public static <S extends PartSlice> SliceGroupAssert assertThat(SliceGroup<S> group) {
        return new SliceGroupAssert(group);
    }

    public static <S extends PartSlice> SliceGroupAssert dessert(SliceGroup<S> group) {
        return assertThat(group);
    }
}
