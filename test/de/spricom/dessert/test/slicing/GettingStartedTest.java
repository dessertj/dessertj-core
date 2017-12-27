package de.spricom.dessert.test.slicing;

import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.SliceAssertions;
import de.spricom.dessert.slicing.SliceContext;
import de.spricom.dessert.slicing.SliceSet;
import org.junit.Test;

import java.io.IOException;

public class GettingStartedTest {

    @Test
    public void checkDessertDependencies() throws IOException {
        SliceContext sc = new SliceContext();
        SliceSet dessert = sc.subPackagesOf("de.spricom.dessert")
                .without(sc.subPackagesOf("de.spricom.dessert.test"));
        SliceSet java = sc.subPackagesOf("java");
        SliceAssertions.assertThat(dessert).usesOnly(java);
    }

    @Test
    public void checkPackagesAreCycleFree() throws IOException {
        SliceSet subPackages = new SliceContext().subPackagesOf("de.spricom.dessert");
        SliceAssertions.dessert(subPackages).isCycleFree();
    }

    @Test
    public void checkNestedPackagesShouldNotUseOuterPackages() throws IOException {
        SliceSet subPackages = new SliceContext().subPackagesOf("de.spricom.dessert");
        for (Slice pckg : subPackages) {
            SliceAssertions.assertThat(pckg).doesNotUse(pckg.getParentPackage());
        }
    }
}
