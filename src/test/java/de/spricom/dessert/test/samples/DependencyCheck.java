package de.spricom.dessert.test.samples;

import java.io.IOException;

import org.fest.assertions.Fail;
import org.junit.Test;

import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.slicing.Slice;
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
    
    @Test
    public void testAssert() throws IOException {
        SliceContext sc = new SliceContext(ClassResolver.ofClassPathAndBootClassPath());
        SliceSet dessert = sc.subPackagesOf("de.spricom.dessert");
        SliceSet io = sc.subPackagesOf("java.io");
        Slice ioSlice = io.iterator().next();
        
        try {
            dessert.forEach(slice -> SliceAssertions.assertThat(slice).doesNotUse(ioSlice));
            Fail.fail("no assertion error");
        } catch (AssertionError ae) {
            System.out.println(ae.getMessage());
        }
    }
}
