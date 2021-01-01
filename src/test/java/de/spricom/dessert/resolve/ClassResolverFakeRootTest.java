package de.spricom.dessert.resolve;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * This test simulates clashes of classes or packages form different roots. It uses a FakeRoot
 * for this purpose, so that it is not necessary to build corresponding JAR's.
 */
public class ClassResolverFakeRootTest {

    @Test
    public void testSimpleFakeRoot() throws IOException {
        ClassResolver resolver = new ClassResolver();
        FakeRoot root = new FakeRoot(new File("/rootx"));
        resolver.addRoot(root);
        root.add("de.sample.Fake");

        assertThat(resolver.getRootFiles()).hasSize(1);
        assertThat(resolver.getRootDirs()).isEmpty();

        ClassPackage cp = resolver.getPackage("de.sample");
        assertThat(cp.getPackageName()).isEqualTo("de.sample");
        assertThat(cp.toString()).isEqualTo("de.sample");
        assertThat(cp.getSubPackages()).isEmpty();
        assertThat(cp.getParent().getPackageName()).isEqualTo("de");

        assertThat(cp.getClasses()).hasSize(1);
        ClassEntry ce = resolver.getClassEntry("de.sample.Fake");
        assertThat(ce.getClassname()).isEqualTo("de.sample.Fake");

        assertThat(ce.getPackage()).isSameAs(cp);
        assertThat(ce.getAlternatives()).isNull();
    }

    @Test
    public void testClashes() throws IOException {
        ClassResolver resolver = new ClassResolver();
        FakeRoot root1 = new FakeRoot(new File("/root1"));
        resolver.addRoot(root1);
        FakeRoot root2 = new FakeRoot(new File("/root2"));
        resolver.addRoot(root2);
        FakeRoot root3 = new FakeRoot(new File("/root3"));
        resolver.addRoot(root3);

        root1.add("de.sample_a.Fake1a");
        root1.add("de.sample_a.Fake2a");
        root1.add("de.sample_a.Fake3a");
        root1.add("de.sample_b.Fake1b");
        root1.add("de.sample_b.Fake2b");
        root1.add("de.sample_b.Fake3b");
        root1.add("de.sample_b.sub.FakeSub1");

        root2.add("de.sample_c.Fake1c");
        root2.add("de.sample_c.Fake2c");
        root2.add("de.sample_c.Fake3c");
        root2.add("de.sample_b.sub.FakeSub2");

        root3.add("de.sample_d.Fake1d");
        root3.add("de.sample_d.Fake2d");
        root3.add("de.sample_d.Fake3d");
        root3.add("de.sample_a.Fake2a");
        root3.add("de.sample_a.Fake3a");
        root3.add("de.sample_a.Fake4a");

        assertThat(resolver.getPackageCount()).isEqualTo(7);
        assertThat(resolver.getClassCount()).isEqualTo(15);

        ClassPackage de1 = resolver.getPackage(root1.getRootFile(),"de");
        assertThat(de1.getClasses()).isEmpty();
        assertThat(de1.getSubPackages()).hasSize(2);
        assertThat(de1.getAlternatives()).hasSize(3);
        assertThat(resolver.getDuplicates()).hasSize(2);
    }
}
