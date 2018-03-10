package de.spricom.dessert.slicing;

public final class PackageSliceAssertions {
    private PackageSliceAssertions() {
    }

    public static SliceAssert dessert(ConcreteSlice set) {
        return assertThat(set);
    }

    public static SliceAssert assertThat(ConcreteSlice set) {
        return new SliceAssert(set);
    }

    public static PackageSliceAssert dessert(PackageSlice packageSlice) {
        return assertThat(packageSlice);
    }

    public static PackageSliceAssert assertThat(PackageSlice packageSlice) {
        return new PackageSliceAssert(packageSlice);
    }
}
