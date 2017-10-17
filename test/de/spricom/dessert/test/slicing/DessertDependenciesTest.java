package de.spricom.dessert.test.slicing;

import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.slicing.SliceContext;
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
    public void test() throws IOException {
        System.out.println(resolver.getRootFiles());
    }
}
