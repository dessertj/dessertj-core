package de.spricom.dessert.slicing;

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
import de.spricom.dessert.classfile.constpool.ConstantPool;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;

public class ClasspathTest {
    private static Classpath cp;

    @BeforeClass
    public static void init() {
        cp = new Classpath();
    }

    @Test
    public void testSliceOfClasses() {
        Slice slice = cp.sliceOf(Slice.class, ClassFile.class, File.class);
        assertThat(slice.getClazzes()).hasSize(3);
    }

    /**
     * Use @code{find target/classes/de/spricom/dessert/classfile/constpool -iname "*.class" | wc -l}
     * to determine the expected result.
     */
    @Test
    public void testPackageTreeForSinglePackage() {
        int expectedNumberOfClasses = 25;
        Slice slice = cp.packageTreeOf(ConstantPool.class);
        assertThat(slice.getClazzes()).hasSize(expectedNumberOfClasses);
    }


    /**
     * The exepected result is the sum of
     * {@code find target/classes/de/spricom/dessert/classfile -iname "*.class" | wc -l} and
     * {@code find target/test-classes/de/spricom/dessert/classfile -iname "*.class" | wc -l}.
     */
    @Test
    public void testPackageTreeForSubpackages() {
        int expectedNumberOfClasses = 89;
        int expectedNumberOfTestClasses = 15;
        Slice slice = cp.packageTreeOf(ClassFile.class);
        assertThat(slice.getClazzes()).hasSize(expectedNumberOfClasses + expectedNumberOfTestClasses);
    }

    @Test
    public void testDeferredSlice() {
        Slice java = cp.slice("java.lang|util..*");
        assertThat(java.contains(cp.asClazz(File.class))).isFalse();
        assertThat(java.contains(cp.asClazz(String.class))).isTrue();
    }

    @Test
    public void testCombiningSlices() {
        Slice lang = cp.slice("java.lang..*");
        Slice util = cp.slice("java.util..*");
        Slice io = cp.slice("java.io..*");

        Slice combined = lang.plus(util).plus(io);

        assertThat(combined.contains(cp.asClazz(File.class))).isTrue();
        assertThat(combined.contains(cp.asClazz(String.class))).isTrue();
        assertThat(combined.contains(cp.asClazz(URL.class))).isFalse();

        assertThat(combined.getClazzes())
                .hasSize(lang.getClazzes().size() + util.getClazzes().size() + io.getClazzes().size());

        assertThat(lang.getClazzes().size()).isGreaterThan(100);
        assertThat(util.getClazzes().size()).isGreaterThan(100);
        assertThat(io.getClazzes().size()).isGreaterThan(100);
    }
}
