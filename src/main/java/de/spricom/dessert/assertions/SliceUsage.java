package de.spricom.dessert.assertions;

import de.spricom.dessert.slicing.Slice;

import java.util.ArrayList;
import java.util.List;

public class SliceUsage {
    private final SliceAssert sliceAssert;
    private final List<Slice> slices = new ArrayList<Slice>();

    SliceUsage(SliceAssert sliceAssert, Slice slice) {
        this.sliceAssert = sliceAssert;
        this.slices.add(slice);
    }

    public SliceUsage and(Slice slice) {
        slices.add(slice);
        return this;
    }

    public SliceAssert only() {
        sliceAssert.usesOnly(slices.toArray(new Slice[slices.size()]));
        return sliceAssert;
    }
}
