package de.spricom.dessert.assertions;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.classfile.constpool.ConstantPool;
import de.spricom.dessert.classfile.dependency.DependencyHolder;
import de.spricom.dessert.groups.PackageSlice;
import de.spricom.dessert.groups.SliceGroup;
import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.SliceContext;
import de.spricom.dessert.slicing.Clazz;
import de.spricom.dessert.util.Predicate;
import de.spricom.dessert.util.SetHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

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
        Slice subPackages = sc.packageTreeOf("de.spricom.dessert")
                .minus(tests());
        SliceAssertions.dessert(subPackages).splitByPackage().isCycleFree();
    }

    /**
     * Enfore a nesting rule: Classes my use neighbour packages or packages
     * that belong to the same package (nested packages), but a class from
     * such a nested package must not use a class from the surrounding package.
     */
    @Test
    public void testNestedPackagesShouldNotUseOuterPackages() {
        SliceGroup<PackageSlice> group = SliceGroup.splitByPackage(sc.packageTreeOf("de.spricom.dessert"));
        for (PackageSlice pckg : group) {
            SliceAssertions.assertThat(pckg).doesNotUse(pckg.getParentPackage(group));
        }
    }

    /**
     * Make sure the whole dessert library does not depend on anyting but the
     * JDK packages specified below.
     */
    @Test
    public void testExternalDependencies() {
        Slice dessert = sc.packageTreeOf("de.spricom.dessert").minus(tests());
        Slice java = sc.packageTreeOf("java.lang")
                .plus(sc.packageTreeOf("java.util"))
                .plus(sc.packageTreeOf("java.io"))
                .plus(sc.packageTreeOf("java.net"))
                .plus(sc.packageTreeOf("java.security"));
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
        Slice classfile = sc.packageTreeOf(ClassFile.class.getPackage()).minus(tests());
        Slice javaCore = sc.packageTreeOf("java.lang")
                .plus(sc.packageTreeOf("java.util"));
        Slice javaIO = sc.packageTreeOf("java.io").plus(javaCore);
        SliceAssertions.assertThat(classfile).usesOnly(javaIO);
        Slice dependencyHolder = sc.packageTreeOf(DependencyHolder.class.getPackage());
        SliceAssertions.assertThat(dependencyHolder).usesOnly(javaCore);
        SliceAssertions.assertThat(sc.packageTreeOf(ConstantPool.class.getPackage()).minus(tests()))
                .usesOnly(javaIO, dependencyHolder);
    }

    /**
     * Checks the dependencies of the dessert library core. The dependencies are
     * slicing -&gt; resolve -&gt; util -&gt; classfile -&gt; java.x. For the classfile
     * package only its facade implemented by the ClassFile class must be used.
     */
    @Test
    public void testDessertDependencies() {
        Slice javaCore = sc.packageTreeOf("java.lang")
                .plus(sc.packageTreeOf("java.util"));
        Slice javaIO = sc.packageTreeOf("java.io")
                .plus(sc.packageTreeOf(URI.class));

        // The ClassFile class is the facade for the classfile package. Nothing but
        // this class should be used outside this package.
        Slice classfile = sc.packageTreeOf(ClassFile.class.getPackage().getName())
                .slice(new Predicate<Clazz>() {
                    @Override
                    public boolean test(Clazz sliceEntry) {
                        return sliceEntry.getName().equals(ClassFile.class.getName());
                    }
                });
        Slice resolve = sc.packageTreeOf(ClassResolver.class.getPackage()).minus(tests());
        Slice slicing = sc.packageTreeOf(Slice.class.getPackage()).minus(tests());
        Slice util = sc.packageTreeOf(SetHelper.class.getPackage());

        SliceAssertions.assertThat(util).usesOnly(javaCore, javaIO);
        SliceAssertions.assertThat(resolve).usesOnly(javaCore, javaIO, classfile, util);
        SliceAssertions.assertThat(slicing)
                .uses(javaCore).and(javaIO).and(resolve).and(util).and(classfile)
                .and(sc.packageTreeOf("java.net"))
                .only();
    }

    private Slice tests() {
        return sc.rootOf(this.getClass());
    }
}
