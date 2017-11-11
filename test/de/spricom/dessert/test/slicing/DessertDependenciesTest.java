package de.spricom.dessert.test.slicing;

import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.slicing.SliceAssertions;
import de.spricom.dessert.slicing.SliceContext;
import de.spricom.dessert.slicing.SliceSet;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class DessertDependenciesTest {
    private static ClassResolver resolver;

    @BeforeClass
    public static void init() throws IOException {
         resolver = ClassResolver.ofClassPathWithoutJars();
    }

    @Test
    public void testPackagesAreCycleFree() throws IOException {
        SliceSet subPackages = new SliceContext().subPackagesOf("de.spricom.dessert");
        SliceAssertions.dessert(subPackages).isCycleFree();
    }
}
