package de.spricom.dessert.test.samples;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.SliceAssertions;
import de.spricom.dessert.slicing.SliceContext;
import de.spricom.dessert.slicing.SliceEntry;
import de.spricom.dessert.slicing.SliceSet;
import de.spricom.dessert.util.DependencyGraph;

public class SpringTest {
    private static ClassResolver resolver;

    private SliceContext sc;

    @Before
    public void init() throws IOException {
        sc = new SliceContext(getHibernateResolver());
    }

    @Test
    public void testCycleFree() throws IOException {
        SliceSet packages = sc.packagesOf(resolver.getRootFiles());
        packages = packages.without(sc.subPackagesOf("org.springframework.cglib.core"));
        packages = packages.without(sc.subPackagesOf("org.springframework.objenesis"));
        SliceAssertions.assertThat(packages).isCycleFree();
    }

    @Test
    public void testClasses() {
        SliceSet packages = sc.packagesOf(resolver.getRootFiles());
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
    
    private boolean isSameClass(SliceEntry s1, SliceEntry s2) {
        return s1.getClassname().startsWith(s2.getClassname()) || s2.getClassname().startsWith(s1.getClassname());
    }

    private static ClassResolver getHibernateResolver() throws IOException {
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
