package de.spricom.dessert.slicing;

public final class SliceAssertions {
    private SliceAssertions() {
    }

    public static SliceAssert dessert(Slice slice) {
        return assertThat(slice);
    }

    public static SliceAssert assertThat(Slice slice) {
        return new SliceAssert(slice.materialize());
    }

    public static PackageSliceAssert dessert(PackageSlice packageSlice) {
        return assertThat(packageSlice);
    }

    public static PackageSliceAssert assertThat(PackageSlice packageSlice) {
        return new PackageSliceAssert(packageSlice);
    }
}
