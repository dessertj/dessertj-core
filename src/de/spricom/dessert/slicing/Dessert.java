package de.spricom.dessert.slicing;

public class Dessert {

    public static SliceSet subPackagesOf(Package pkg) {
        return new SliceSet();
    }

    public static SliceSetAssert dessert(SliceSet subPackages) {
        return assertThat(subPackages);
    }

    public static SliceSetAssert assertThat(SliceSet subPackages) {
        return new SliceSetAssert();
    }

    public static SliceAssert assertThat(Slice slice) {
        return new SliceAssert();
    }

}
