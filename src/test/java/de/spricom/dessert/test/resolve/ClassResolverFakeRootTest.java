package de.spricom.dessert.test.resolve;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.resolve.ClassFileEntry;
import de.spricom.dessert.resolve.ClassPackage;
import de.spricom.dessert.resolve.ClassResolver;
import org.junit.Test;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

/**
 * This test simulates clashes of classes or packages form different roots. It uses a FakeRoot
 * for this purpose, so that it is not necessary to build corresponding JAR's.
 */
public class ClassResolverFakeRootTest {

    @Test
    public void testSimpleFakeRoot() {
        ClassResolver resolver = new ClassResolver();
        FakeRoot root = new FakeRoot(resolver, new File("/root"));
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
        ClassFileEntry cf = resolver.getClassFile("de.sample.Fake");
        assertThat(cf.getFilename()).isEqualTo("root");
        assertThat(cf.getClassname()).isEqualTo("de.sample.Fake");
        assertThat(cf.getClassfile().getThisClass()).isEqualTo(FakeClassFileEntry.class.getName());

        assertThat(cf.getPackage()).isSameAs(cp);
        assertThat(cf.getAlternatives()).isNull();
    }
}
