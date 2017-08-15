package de.spricom.dessert.test.samples;

import java.io.File;
import java.io.IOException;

import org.fest.assertions.Fail;
import org.junit.Test;

import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.slicing.SliceAssertions;
import de.spricom.dessert.slicing.SliceContext;
import de.spricom.dessert.slicing.SliceSet;
import junit.framework.AssertionFailedError;

public class HibernateTest {
    private static ClassResolver resolver;

    @Test
    public void testCycleFree() throws IOException {
        SliceContext sc = new SliceContext(getHibernateResolver());
        SliceSet packages = sc.packagesOf(resolver.getRootFiles());
        try {
            SliceAssertions.assertThat(packages).isCycleFree();
            Fail.fail("No cycle found");
        } catch (AssertionFailedError ae) {
            System.out.println(ae.toString());
        }
    }
    
    private static ClassResolver getHibernateResolver() throws IOException {
        if (resolver == null) {
            resolver = new ClassResolver();
            for (String filename : System.getProperty("java.class.path").split(File.pathSeparator)) {
                if (filename.contains("hibernate")) {
                    resolver.add(filename);
                }
            }
        }
        return resolver;
    }
}
