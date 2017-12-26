package de.spricom.dessert.test.slicing;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.classfile.attribute.AttributeInfo;
import de.spricom.dessert.classfile.dependency.DependencyHolder;
import de.spricom.dessert.duplicates.DuplicateClassFinder;
import de.spricom.dessert.resolve.ClassPredicate;
import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.slicing.*;
import de.spricom.dessert.traversal.ClassVisitor;
import de.spricom.dessert.util.SetHelper;
import javassist.bytecode.ConstPool;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class DessertDependenciesTest {
    private static SliceContext sc;

    @BeforeClass
    public static void init() throws IOException {
        ClassResolver resolver = ClassResolver.ofClassPathWithoutJars();
        resolver.addBootClassPath();
        sc = new SliceContext(resolver);
    }

    @Test
    public void testPackagesAreCycleFree() throws IOException {
        SliceSet subPackages = sc.subPackagesOf("de.spricom.dessert");
        SliceAssertions.dessert(subPackages).isCycleFree();
    }

    @Test
    public void testNestedPackagesShouldNotUseOuterPackages() {
        SliceSet subPackages = sc.subPackagesOf("de.spricom.dessert");
        for (Slice pckg : subPackages) {
            SliceAssertions.assertThat(pckg).doesNotUse(pckg.getParentPackage());
        }
    }

    @Test
    public void testExternalDependencies() throws IOException {
        SliceSet dessert = sc.subPackagesOf("de.spricom.dessert")
                .without(sc.subPackagesOf("de.spricom.dessert.test"));
        SliceSet java = sc.subPackagesOf("java.lang")
                .with(sc.subPackagesOf("java.util"))
                .with(sc.subPackagesOf("java.io"))
                .with(sc.subPackagesOf("java.net"))
                .with(sc.subPackagesOf("java.security"));
        SliceAssertions.assertThat(dessert).usesOnly(java);
    }

    @Test
    public void testClassfileDependencies() {
        SliceSet classfile = sc.subPackagesOf(ClassFile.class.getPackage());
        SliceSet javaCore = sc.subPackagesOf("java.lang")
                .with(sc.subPackagesOf("java.util"));
        SliceSet javaIO = sc.subPackagesOf("java.io").with(javaCore);
        SliceAssertions.assertThat(classfile).usesOnly(javaIO);
        SliceAssertions.assertThat(sc.subPackagesOf(DependencyHolder.class.getPackage())).usesOnly(javaCore);
        SliceAssertions.assertThat(sc.subPackagesOf(ConstPool.class.getPackage())).usesOnly(javaCore);
    }

    @Test
    public void testPackageDependencies() {
        SliceSet javaCore = sc.subPackagesOf("java.lang")
                .with(sc.subPackagesOf("java.util"));
        SliceSet javaIO = sc.subPackagesOf("java.io");
        SliceSet classfile = sc.subPackagesOf(ClassFile.class.getPackage().getName())
                .slice(new ClassPredicate<SliceEntry>() {
                    @Override
                    public boolean test(SliceEntry sliceEntry) {
                        return sliceEntry.getClassname().equals(ClassFile.class.getName());
                    }
                });
        SliceSet resolve = sc.subPackagesOf(ClassResolver.class.getPackage());
        SliceSet slicing = sc.subPackagesOf(SliceSet.class.getPackage());
        SliceSet util = sc.subPackagesOf(SetHelper.class.getPackage());

        SliceAssertions.assertThat(util).usesOnly(javaCore);
        SliceAssertions.assertThat(resolve).usesOnly(javaCore, javaIO, classfile);
        SliceAssertions.assertThat(slicing)
                .uses(javaCore).and(javaIO).and(resolve).and(util).and(classfile)
                .and(sc.subPackagesOf("java.net"))
                .only();
    }

    @Test
    public void testDuplicatesDependencies() {
        SliceSet duplicates = sc.subPackagesOf(DuplicateClassFinder.class.getPackage());
        SliceSet traversal = sc.subPackagesOf(ClassVisitor.class.getPackage());
        SliceSet java = sc.subPackagesOf("java.lang")
                .with(sc.subPackagesOf("java.util"))
                .with(sc.subPackagesOf("java.io"));

        SliceAssertions.assertThat(sc.subPackagesOf("de.spricom.dessert")
                .without(sc.subPackagesOf("de.spricom.dessert.test"))
                .without(duplicates))
                .doesNotUse(duplicates);
        SliceAssertions.assertThat(traversal).uses(java).only();
        SliceAssertions.assertThat(duplicates).uses(java).and(traversal)
                .and(sc.subPackagesOf("java.security"))
                .only();
    }
}
