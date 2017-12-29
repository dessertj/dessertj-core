package de.spricom.dessert.test.slicing;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.classfile.constpool.ConstantPool;
import de.spricom.dessert.classfile.dependency.DependencyHolder;
import de.spricom.dessert.duplicates.DuplicateClassFinder;
import de.spricom.dessert.resolve.ClassPredicate;
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
public class DessertDependenciesTest {

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
        resolver.addBootClassPath();
        sc = new SliceContext(resolver);
    }

    /**
     * Make sure there are no cyclic dependencies between dessert packages.
     */
    @Test
    public void testPackagesAreCycleFree() {
        SliceSet subPackages = sc.subPackagesOf("de.spricom.dessert");
        SliceAssertions.dessert(subPackages).isCycleFree();
    }

    /**
     * Enfore a nesting rule: Classes my use neighbour packages or packages
     * that belong to the same package (nested packages), but a class from
     * such a nested package must not use a class from the surrounding package.
     */
    @Test
    public void testNestedPackagesShouldNotUseOuterPackages() {
        SliceSet subPackages = sc.subPackagesOf("de.spricom.dessert");
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
        SliceSet dessert = sc.subPackagesOf("de.spricom.dessert")
                .without(sc.subPackagesOf("de.spricom.dessert.test"));
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
        SliceSet classfile = sc.subPackagesOf(ClassFile.class.getPackage());
        SliceSet javaCore = sc.subPackagesOf("java.lang")
                .with(sc.subPackagesOf("java.util"));
        SliceSet javaIO = sc.subPackagesOf("java.io").with(javaCore);
        SliceAssertions.assertThat(classfile).usesOnly(javaIO);
        SliceSet dependencyHolder = sc.subPackagesOf(DependencyHolder.class.getPackage());
        SliceAssertions.assertThat(dependencyHolder).usesOnly(javaCore);
        SliceAssertions.assertThat(sc.subPackagesOf(ConstantPool.class.getPackage())).usesOnly(javaIO, dependencyHolder);
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

    /**
     * Checks the dependencies of the DuplicateCLassFinder. Currently the implementation of the
     * DuplicateClassFinder is completely independent from the dessert core.
     */
    @Test
    public void testDuplicateClassFinderDependencies() {
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
