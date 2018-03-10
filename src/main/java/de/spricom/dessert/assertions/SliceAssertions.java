package de.spricom.dessert.assertions;

import de.spricom.dessert.slicing.PackageSlice;
import de.spricom.dessert.slicing.PackageSliceAssert;
import de.spricom.dessert.slicing.Slice;

public final class SliceAssertions {
    private SliceAssertions() {
    }

    public static CustomRendererAssertions renderWith(IllegalDependenciesRenderer renderer) {
        return new CustomRendererAssertions(renderer);
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
