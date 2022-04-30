package de.spricom.dessert.assertions;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2022 Hans Jörg Heßmann
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.classfile.attribute.AttributeInfo;
import de.spricom.dessert.classfile.constpool.ConstantPool;
import de.spricom.dessert.classfile.dependency.DependencyHolder;
import de.spricom.dessert.matching.NamePattern;
import de.spricom.dessert.modules.ModuleRegistry;
import de.spricom.dessert.modules.core.ModuleSlice;
import de.spricom.dessert.modules.fixed.JavaModules;
import de.spricom.dessert.modules.java.JavaModulesResolver;
import de.spricom.dessert.modules.jdk.JdkModulesResolver;
import de.spricom.dessert.modules.jpms.JavaPlatformModuleResolver;
import de.spricom.dessert.partitioning.ClazzPredicates;
import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.slicing.Classpath;
import de.spricom.dessert.slicing.PackageSlice;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.Slices;
import de.spricom.dessert.util.Sets;
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
     * The same Classpath is used for all tests.
     */
    private static final Classpath cp = new Classpath();
    private final Slice main = cp.rootOf(Slice.class).minus(ClazzPredicates.DEPRECATED);
    private final Slice test = cp.rootOf(this.getClass());

    /**
     * Make sure there are no cyclic dependencies between dessert packages.
     */
    @Test
    public void testPackagesAreCycleFree() {
        dessert(main.partitionByPackage()).isCycleFree();
    }

    /**
     * Enfore a nesting rule: Classes my use neighbour packages or packages
     * that belong to the same package (nested packages), but a class from
     * such a nested package must not use a class from the surrounding package.
     */
    @Test
    public void testNestedPackagesShouldNotUseOuterPackages() {
        Slice mainAndTest = main.plus(test);
        SortedMap<String, PackageSlice> packages = mainAndTest.partitionByPackage();

        for (PackageSlice pckg : packages.values()) {
            dessert(pckg).usesNot(pckg.getParentPackage());
        }
    }

    /**
     * Make sure the whole dessert library does not depend on anyting but the
     * JDK packages specified below.
     */
    @Test
    public void testExternalDependencies() {
        Slice deprecated = cp.rootOf(Slice.class).slice(ClazzPredicates.DEPRECATED);
        Slice java = Slices.of(
                cp.packageTreeOf("java.lang"),
                cp.packageTreeOf("java.util"),
                cp.packageTreeOf("java.io"),
                cp.packageTreeOf("java.net"));
        dessert(main).usesOnly(java, deprecated);
    }

    @Test
    public void testPackageOrder() {
        Map<String, PackageSlice> packages = main.partitionByPackage();
        List<PackageSlice> layers = Arrays.asList(
                packages.remove(packageOf(SliceAssertions.class)),
                packages.remove(packageOf(ClazzPredicates.class)),
                packages.remove(packageOf(ModuleRegistry.class)),
                packages.remove(packageOf(JavaModules.class)),
                packages.remove(packageOf(JdkModulesResolver.class)),
                packages.remove(packageOf(JavaModulesResolver.class)),
                packages.remove(packageOf(JavaPlatformModuleResolver.class)),
                packages.remove(packageOf(ModuleSlice.class)),
                packages.remove(packageOf(Slice.class)),
                packages.remove(packageOf(ClassResolver.class)),
                packages.remove(packageOf(ClassFile.class)),
                packages.remove(packageOf(AttributeInfo.class)),
                packages.remove(packageOf(ConstantPool.class)),
                packages.remove(packageOf(DependencyHolder.class)),
                packages.remove(packageOf(NamePattern.class)),
                packages.remove(packageOf(Sets.class)));
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
        Slice classfile = cp.packageTreeOf(ClassFile.class.getPackage()).minus(test);
        Slice javaCore = cp.packageTreeOf("java.lang")
                .plus(cp.packageTreeOf("java.util"));
        Slice javaIO = cp.packageTreeOf("java.io").plus(javaCore);
        SliceAssertions.dessert(classfile).usesOnly(javaIO);

        // Packages outside the 'classfile' package must not use anything but the ClassFile facade.
        // The only exception are modules which may use attributes.
        Slice attributes = classfile.slice("..classfile.attribute.*");
        dessert(main.minus(classfile, cp.asClazz("de.spricom.dessert.partitioning.AnnotationMatcher")))
                .usesNot(classfile.minus(cp.asClazz(ClassFile.class), attributes));
        dessert(main.minus(classfile).minus("..dessert.modules..*")
                .minus("..dessert.partitioning.AnnotationMatcher")
                .minus("..dessert.slicing.Clazz"))
                .usesNot(classfile.minus(cp.asClazz(ClassFile.class)));
    }
}
