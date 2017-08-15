package de.spricom.dessert.test.samples;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.fest.assertions.Fail;
import org.junit.Before;
import org.junit.Test;

import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.SliceAssertions;
import de.spricom.dessert.slicing.SliceContext;
import de.spricom.dessert.slicing.SliceEntry;
import de.spricom.dessert.slicing.SliceSet;
import de.spricom.dessert.util.DependencyGraph;
import junit.framework.AssertionFailedError;

public class HibernateTest {
    private static ClassResolver resolver;

    private SliceContext sc;

    @Before
    public void init() throws IOException {
        sc = new SliceContext(getHibernateResolver());
    }

    @Test
    public void testCycleFree() throws IOException {
        SliceSet packages = sc.packagesOf(resolver.getRootFiles());
        try {
            SliceAssertions.assertThat(packages).isCycleFree();
            Fail.fail("No cycle found");
        } catch (AssertionFailedError ae) {
            System.out.println(ae.toString());
        }
    }

    @Test
    public void testClasses() {
        SliceSet packages = sc.subPackagesOf("org.hibernate");
        Set<SliceEntry> classes = new HashSet<>();
        for (Slice slice : packages) {
            classes.addAll(slice.getEntries());
        }
        DependencyGraph<SliceEntry> dag = new DependencyGraph<>();
        for (SliceEntry n : classes) {
            for (SliceEntry m : classes) {
                if (n != m && !n.getPackageName().equals(m.getPackageName()) && n.getUsedClasses().contains(m)) {
                    dag.addDependency(n, m);
                }
            }
        }
        assertThat(dag.isCycleFree()).isFalse();
        System.out.println("Class-Cycle:");
        for (SliceEntry entry : dag.getCycle()) {
            System.out.println("-> " + entry.getClassname());
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
