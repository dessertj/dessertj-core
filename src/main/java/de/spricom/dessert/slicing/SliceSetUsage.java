package de.spricom.dessert.slicing;

import java.util.ArrayList;
import java.util.List;

public class SliceSetUsage {
    private final SliceSetAssert sliceSetAssert;
    private final List<SliceSet> sliceSets = new ArrayList<SliceSet>();

    SliceSetUsage(SliceSetAssert sliceSetAssert, SliceSet sliceSet) {
        this.sliceSetAssert = sliceSetAssert;
        this.sliceSets.add(sliceSet);
    }

    public SliceSetUsage and(SliceSet sliceSet) {
        sliceSets.add(sliceSet);
        return this;
    }

    public SliceSetAssert only() {
        sliceSetAssert.usesOnly(sliceSets.toArray(new SliceSet[sliceSets.size()]));
        return sliceSetAssert;
    }
}
