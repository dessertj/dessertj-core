package de.spricom.dessert.assertions;

import de.spricom.dessert.slicing.Slice;

public class CustomRendererAssertions {
    private final DependencyViolationsRenderer violationsRenderer;

    CustomRendererAssertions(DependencyViolationsRenderer violationsRenderer) {
        this.violationsRenderer = violationsRenderer;
    }

    public SliceAssert dessert(Slice slice) {
        return assertThat(slice);
    }

    public SliceAssert assertThat(Slice slice) {
        return new SliceAssert(slice.materialize()).renderWith(violationsRenderer);
    }
}
