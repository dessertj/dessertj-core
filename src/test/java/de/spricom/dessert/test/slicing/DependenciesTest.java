package de.spricom.dessert.test.slicing;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.classfile.constpool.ConstantPool;
import de.spricom.dessert.classfile.dependency.DependencyHolder;
import de.spricom.dessert.duplicates.DuplicateClassFinder;
import de.spricom.dessert.util.Predicate;
import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.slicing.*;
import de.spricom.dessert.traversal.ClassVisitor;
import de.spricom.dessert.util.SetHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

/**
 * This test checks the dependencies of the dessert library. It's an example on how
 * to use it.
 */
public class DependenciesTest {

    /**
     * The same SliceContext is used for all tests.
     */
    private static SliceContext sc;

    /**
     * For performance reasons the SliceContext uses a specialized resolver that
     * processes the compiled class and the java runtime classes, only. The default
     * implementation would consider the whole CLASSPATH.
     */
    @BeforeClass
    public static void init() throws IOException {
        ClassResolver resolver = ClassResolver.ofClassPathWithoutJars();
        sc = new SliceContext(resolver);
    }

    /**
     * Make sure there are no cyclic dependencies between dessert packages.
     */
    @Test
    public void testPackagesAreCycleFree() {
        ManifestSliceSet subPackages = sc.subPackagesOfManifested("de.spricom.dessert");
        SliceAssertions.dessert(subPackages).isCycleFree();
    }

    /**
     * Enfore a nesting rule: Classes my use neighbour packages or packages
     * that belong to the same package (nested packages), but a class from
     * such a nested package must not use a class from the surrounding package.
     */
    @Test
    public void testNestedPackagesShouldNotUseOuterPackages() {
        ManifestSliceSet subPackages = sc.subPackagesOfManifested("de.spricom.dessert");
        for (Slice pckg : subPackages) {
            SliceAssertions.assertThat(pckg).doesNotUse(pckg.getParentPackage());
        }
    }

    /**
     * Make sure the whole dessert library does not depend on anyting but the
     * JDK packages specified below.
     */
    @Test
    public void testExternalDependencies() {
        ManifestSliceSet dessert = sc.subPackagesOfManifested("de.spricom.dessert")
                .without(sc.subPackagesOfManifested("de.spricom.dessert.test"));
        SliceSet java = sc.subPackagesOf("java.lang")
                .with(sc.subPackagesOf("java.util"))
                .with(sc.subPackagesOf("java.io"))
                .with(sc.subPackagesOf("java.net"))
                .with(sc.subPackagesOf("java.security"));
        SliceAssertions.assertThat(dessert).usesOnly(java);
    }

    /**
     * Check the dependencies inside the classfile package: The classfile package as a whole
     * uses java.lang, java.util and java.io, only. The dependency sub-packages that consists
     * of a single interface uses only java.long and java.util. The constpool sub-package
     * uses java.lang, java.util, java.io and the dependency sub-package. The attribute
     * sub-packages has the same dependencies as the classes directly located in the classfile
     * packages, thus there is no explicit check for the attribute subpackage required.
     */
    @Test
    public void testClassfileDependencies() {
        ManifestSliceSet classfile = sc.subPackagesOfManifested(ClassFile.class.getPackage());
        SliceSet javaCore = sc.subPackagesOf("java.lang")
                .with(sc.subPackagesOf("java.util"));
        SliceSet javaIO = sc.subPackagesOf("java.io").with(javaCore);
        SliceAssertions.assertThat(classfile).usesOnly(javaIO);
        ManifestSliceSet dependencyHolder = sc.subPackagesOfManifested(DependencyHolder.class.getPackage());
        SliceAssertions.assertThat(dependencyHolder).usesOnly(javaCore);
        SliceAssertions.assertThat(sc.subPackagesOfManifested(ConstantPool.class.getPackage())).usesOnly(javaIO, dependencyHolder);
    }

    /**
     * Checks the dependencies of the dessert library core. The dependencies are
     * slicing -&gt; resolve -&gt; util -&gt; classfile -&gt; java.x. For the classfile
     * package only its facade implemented by the ClassFile class must be used.
     */
    @Test
    public void testDessertDependencies() {
        SliceSet javaCore = sc.subPackagesOf("java.lang")
                .with(sc.subPackagesOf("java.util"));
        SliceSet javaIO = sc.subPackagesOf("java.io");

        // The ClassFile class is the facade for the classfile package. Nothing but
        // this class should be used outside this package.
        SliceSet classfile = sc.subPackagesOf(ClassFile.class.getPackage().getName())
                .slice(new Predicate<SliceEntry>() {
                    @Override
                    public boolean test(SliceEntry sliceEntry) {
                        return sliceEntry.getClassname().equals(ClassFile.class.getName());
                    }
                });
        ManifestSliceSet resolve = sc.subPackagesOfManifested(ClassResolver.class.getPackage());
        ManifestSliceSet slicing = sc.subPackagesOfManifested(ManifestSliceSet.class.getPackage());
        ManifestSliceSet util = sc.subPackagesOfManifested(SetHelper.class.getPackage());

        SliceAssertions.assertThat(util).usesOnly(javaCore);
        SliceAssertions.assertThat(resolve).usesOnly(javaCore, javaIO, classfile, util);
        SliceAssertions.assertThat(slicing)
                .uses(javaCore).and(javaIO).and(resolve).and(util).and(classfile)
                .and(sc.subPackagesOf("java.net"))
                .only();
    }

    /**
     * Checks the dependencies of the DuplicateCLassFinder. Currently the implementation of the
     * DuplicateClassFinder is completely independent from the dessert core.
     */
    @Test
    public void testDuplicateClassFinderDependencies() {
        ManifestSliceSet duplicates = sc.subPackagesOfManifested(DuplicateClassFinder.class.getPackage());
        ManifestSliceSet traversal = sc.subPackagesOfManifested(ClassVisitor.class.getPackage());
        SliceSet java = sc.subPackagesOf("java.lang")
                .with(sc.subPackagesOf("java.util"))
                .with(sc.subPackagesOf("java.io"));

        SliceAssertions.assertThat(sc.subPackagesOfManifested("de.spricom.dessert")
                .without(sc.subPackagesOfManifested("de.spricom.dessert.test"))
                .without(duplicates))
                .doesNotUse(duplicates);
        SliceAssertions.assertThat(traversal).uses(java).only();
        SliceAssertions.assertThat(duplicates).uses(java).and(traversal)
                .and(sc.subPackagesOf("java.security"))
                .only();
    }
}
