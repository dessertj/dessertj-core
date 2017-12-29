package de.spricom.dessert.slicing;

public final class SliceAssertions {
    private SliceAssertions() {
    }

    public static SliceSetAssert dessert(SliceSet set) {
        return assertThat(set);
    }

    public static SliceSetAssert assertThat(SliceSet set) {
        return new SliceSetAssert(set);
    }

    public static SliceAssert dessert(Slice slice) {
        return assertThat(slice);
    }

    public static SliceAssert assertThat(Slice slice) {
        return new SliceAssert(slice);
    }
}
