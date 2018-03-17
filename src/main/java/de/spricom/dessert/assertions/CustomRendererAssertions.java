package de.spricom.dessert.assertions;

import de.spricom.dessert.slicing.Slice;

public class CustomRendererAssertions {
    private final IllegalDependenciesRenderer illegalDependenciesRenderer;

    CustomRendererAssertions(IllegalDependenciesRenderer illegalDependenciesRenderer) {
        this.illegalDependenciesRenderer = illegalDependenciesRenderer;
    }

    public SliceAssert dessert(Slice slice) {
        return assertThat(slice);
    }

    public SliceAssert assertThat(Slice slice) {
        return new SliceAssert(slice).renderWith(illegalDependenciesRenderer);
    }
}
