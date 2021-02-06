package de.spricom.dessert.slicing;

/*-
 * #%L
 * Dessert Dependency Assertion Library
 * %%
 * Copyright (C) 2017 - 2021 Hans Jörg Heßmann
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

import static org.fest.assertions.Assertions.assertThat;

public class ClasspathTest {
    private static Classpath sc;

    @BeforeClass
    public static void init() {
        sc = new Classpath();
    }

    @Test
    public void testSliceOfClasses() {
        Slice slice = sc.sliceOf(Slice.class, ClassFile.class, File.class);
        assertThat(slice.getClazzes()).hasSize(3);
    }

    /**
     * Use @code{find target/classes/de/spricom/dessert/classfile/constpool -iname "*.class" | wc -l}
     * to determine the expected result.
     */
    @Test
    public void testPackageTreeForSinglePackage() {
        int expectedNumberOfClasses = 25;
        Slice slice = sc.packageTreeOf(ConstantPool.class);
        assertThat(slice.getClazzes()).hasSize(expectedNumberOfClasses);
    }


    /**
     * The exepected result is the sum of
     * @code{find target/classes/de/spricom/dessert/classfile -iname "*.class" | wc -l} and
     * @code{find target/test-classes/de/spricom/dessert/classfile -iname "*.class" | wc -l}.
     */
    @Test
    public void testPackageTreeForSubpackages() {
        int expectedNumberOfClasses = 46;
        int expectedNumberOfTestClasses = 14;
        Slice slice = sc.packageTreeOf(ClassFile.class);
        assertThat(slice.getClazzes()).hasSize(expectedNumberOfClasses + expectedNumberOfTestClasses);
    }
}
