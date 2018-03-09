package de.spricom.dessert.slicing;

public final class SliceAssertions {
    private SliceAssertions() {
    }

    public static SliceSetAssert dessert(ManifestSliceSet set) {
        return assertThat(set);
    }

    public static SliceSetAssert assertThat(ManifestSliceSet set) {
        return new SliceSetAssert(set);
    }

    public static SliceAssert dessert(Slice slice) {
        return assertThat(slice);
    }

    public static SliceAssert assertThat(Slice slice) {
        return new SliceAssert(slice);
    }
}
