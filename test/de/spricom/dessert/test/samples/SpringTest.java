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

public class SpringTest {
    private static ClassResolver resolver;

    private SliceContext sc;
    private SliceSet packages;

    @Before
    public void init() throws IOException {
        sc = new SliceContext(getSpringJarsResolver());
        packages = sc.packagesOf(resolver.getRootFiles());
    }

    @Test
    public void testPackageCycles() throws IOException {
        packages = packages.without(sc.subPackagesOf("org.springframework.cglib.core"));
        packages = packages.without(sc.subPackagesOf("org.springframework.objenesis"));
        SliceAssertions.assertThat(packages).isCycleFree();
    }

    @Test
    public void testClassCycles() {
        Set<SliceEntry> classes = new HashSet<>();
        for (Slice slice : packages) {
            classes.addAll(slice.getEntries());
        }
        DependencyGraph<SliceEntry> dag = new DependencyGraph<>();
        for (SliceEntry n : classes) {
            for (SliceEntry m : classes) {
                if (n != m && !isSameClass(n, m) && n.getUsedClasses().contains(m)) {
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

    @Test
    public void testNestedPackageDependencies() {
        try {
            for (Slice slice : packages) {
                SliceAssertions.assertThat(slice).doesNotUse(slice.getParentPackage());
            }
            Fail.fail("No dependency found");
        } catch (AssertionError ae) {
            System.out.println(ae.getMessage());
        }
    }

    @Test
    public void testOuterPackageDependencies() {
        try {
            for (Slice slice : packages) {
                SliceAssertions.assertThat(slice.getParentPackage()).doesNotUse(slice);
            }
            Fail.fail("No dependency found");
        } catch (AssertionError ae) {
            System.out.println(ae.getMessage());
        }
    }

    private boolean isSameClass(SliceEntry s1, SliceEntry s2) {
        return s1.getClassname().startsWith(s2.getClassname()) || s2.getClassname().startsWith(s1.getClassname());
    }

    private static ClassResolver getSpringJarsResolver() throws IOException {
        if (resolver == null) {
            resolver = new ClassResolver();
            for (String filename : System.getProperty("java.class.path").split(File.pathSeparator)) {
                if (filename.contains("spring")) {
                    resolver.add(filename);
                }
            }
        }
        return resolver;
    }
}
