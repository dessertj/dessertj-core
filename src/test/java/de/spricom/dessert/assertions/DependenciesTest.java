package de.spricom.dessert.assertions;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.classfile.attribute.AttributeInfo;
import de.spricom.dessert.classfile.constpool.ConstantPool;
import de.spricom.dessert.classfile.dependency.DependencyHolder;
import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.slicing.PackageSlice;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.SliceContext;
import de.spricom.dessert.slicing.Slices;
import de.spricom.dessert.util.SetHelper;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import static de.spricom.dessert.assertions.SliceAssertions.dessert;

/**
 * This test checks the dependencies of the dessert library. It's an example on how
 * to use it.
 */
public class DependenciesTest {

    /**
     * The same SliceContext is used for all tests.
     */
    private static final SliceContext sc = new SliceContext();
    private final Slice main = sc.rootOf(Slice.class);
    private final Slice test = sc.rootOf(this.getClass());

    /**
     * Make sure there are no cyclic dependencies between dessert packages.
     */
    @Test
    public void testPackagesAreCycleFree() {
        dessert(main.splitByPackage()).isCycleFree();
    }

    /**
     * Enfore a nesting rule: Classes my use neighbour packages or packages
     * that belong to the same package (nested packages), but a class from
     * such a nested package must not use a class from the surrounding package.
     */
    @Test
    public void testNestedPackagesShouldNotUseOuterPackages() {
        Slice mainAndTest = main.plus(test);
        SortedMap<String, PackageSlice> packages = mainAndTest.splitByPackage();

        for (PackageSlice pckg : packages.values()) {
            dessert(pckg).doesNotUse(pckg.getParentPackage());
        }
    }

    /**
     * Make sure the whole dessert library does not depend on anyting but the
     * JDK packages specified below.
     */
    @Test
    public void testExternalDependencies() {
        Slice java = Slices.of(
                sc.packageTreeOf("java.lang"),
                sc.packageTreeOf("java.util"),
                sc.packageTreeOf("java.io"),
                sc.packageTreeOf("java.net"));
        dessert(main).usesOnly(java);
    }

    @Test
    public void testPackageOrder() {
        Map<String, PackageSlice> packages = main.splitByPackage();
        List<PackageSlice> layers = Arrays.asList(
                packages.remove(packageOf(SliceAssertions.class)),
                packages.remove(packageOf(Slice.class)),
                packages.remove(packageOf(ClassResolver.class)),
                packages.remove(packageOf(ClassFile.class)),
                packages.remove(packageOf(AttributeInfo.class)),
                packages.remove(packageOf(ConstantPool.class)),
                packages.remove(packageOf(DependencyHolder.class)),
                packages.remove(packageOf(SetHelper.class)));
        dessert(layers).isLayeredRelaxed();
        Assertions.assertThat(packages).isEmpty();
    }

    private String packageOf(Class<?> clazz) {
        return clazz.getPackage().getName();
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
        Slice classfile = sc.packageTreeOf(ClassFile.class.getPackage()).minus(test);
        Slice javaCore = sc.packageTreeOf("java.lang")
                .plus(sc.packageTreeOf("java.util"));
        Slice javaIO = sc.packageTreeOf("java.io").plus(javaCore);
        SliceAssertions.assertThat(classfile).usesOnly(javaIO);

        // Packages outside the 'classfile' package must not use anything but the ClassFile facade.
        dessert(main.minus(classfile)).doesNotUse(classfile.minus(sc.asClazz(ClassFile.class)));
    }
}
