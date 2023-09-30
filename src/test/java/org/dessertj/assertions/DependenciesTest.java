package org.dessertj.assertions;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2023 Hans Jörg Heßmann
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

import org.dessertj.classfile.ClassFile;
import org.dessertj.classfile.attribute.AttributeInfo;
import org.dessertj.classfile.constpool.ConstantPool;
import org.dessertj.classfile.dependency.DependencyHolder;
import org.dessertj.matching.NamePattern;
import org.dessertj.modules.ModuleRegistry;
import org.dessertj.modules.core.ModuleSlice;
import org.dessertj.modules.fixed.JavaModules;
import org.dessertj.modules.java.JavaModulesResolver;
import org.dessertj.modules.jdk.JdkModulesResolver;
import org.dessertj.modules.jpms.JavaPlatformModuleResolver;
import org.dessertj.partitioning.ClazzPredicates;
import org.dessertj.resolve.ClassResolver;
import org.dessertj.slicing.Classpath;
import org.dessertj.slicing.PackageSlice;
import org.dessertj.slicing.Slice;
import org.dessertj.util.Sets;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import static org.dessertj.assertions.SliceAssertions.dessert;

/**
 * This test checks the dependencies of the dessertj library. It's an example on how
 * to use it.
 */
public class DependenciesTest {

    /**
     * The same Classpath is used for all tests.
     */
    private static final Classpath cp = new Classpath();
    private static final ModuleRegistry mr = new ModuleRegistry(cp);
    private static final JavaModules java = new JavaModules(mr);
    private final Slice main = cp.rootOf(Slice.class).minus(ClazzPredicates.DEPRECATED);
    private final Slice test = cp.rootOf(this.getClass());

    /**
     * Make sure there are no cyclic dependencies between dessertj packages.
     */
    @Test
    public void testPackagesAreCycleFree() {
        dessert(main.partitionByPackage()).isCycleFree();
    }

    /**
     * Enforce a nesting rule: Classes my use neighbour packages or packages
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
     * Make sure the whole dessertj library does not depend on anything but the
     * classes from modules specified below.
     */
    @Test
    public void testExternalDependencies() {
        dessert(main).usesOnly(java.base, java.logging);
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
        packages.remove("");
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
        dessert(classfile).usesOnly(javaIO);

        // Packages outside the 'classfile' package must not use anything but the ClassFile facade.
        // The only exception are modules which may use attributes.
        Slice attributes = classfile.slice("..classfile.attribute.*");
        dessert(main.minus(classfile, cp.asClazz("org.dessertj.partitioning.AnnotationMatcher")))
                .usesNot(classfile.minus(cp.asClazz(ClassFile.class), attributes));
        dessert(main.minus(classfile).minus("..dessertj.modules..*")
                .minus("..dessertj.partitioning.AnnotationMatcher")
                .minus("..dessertj.slicing.Clazz"))
                .usesNot(classfile.minus(cp.asClazz(ClassFile.class)));
    }
}
