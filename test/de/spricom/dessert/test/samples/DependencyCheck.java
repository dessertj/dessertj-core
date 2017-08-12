package de.spricom.dessert.test.samples;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import de.spricom.dessert.slicing.SliceAssertions;
import de.spricom.dessert.slicing.SliceContext;
import de.spricom.dessert.slicing.SliceSet;

public class DependencyCheck {

    @Test
    public void testPackageDependencies() throws IOException {
        SliceSet subPackages = new SliceContext().subPackagesOf("de.spricom.dessert");
        SliceAssertions.dessert(subPackages).isCycleFree();
        
        subPackages.forEach(slice -> SliceAssertions.assertThat(slice).doesNotUse(slice.getParentPackage()));
    }
    
    public void checkPackageApi() {
        SortedSet<String> set = new TreeSet<>();
        for (Package p : Package.getPackages()) {
            set.add(p.getName());
        }
        set.forEach(s -> System.out.println(s));
    }
}
