package org.dessertj.samples;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2025 Hans Jörg Heßmann
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
import org.dessertj.samples.generics.EmptyGenericWithBounds;
import org.dessertj.samples.generics.GenericWithoutBounds;
import org.dessertj.samples.generics.GenericWithoutBoundsEmpty;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

/**
 * This test has been generated by TestGeneratorTool.
 */
public class GenericsTest {

    @Test
    public void testEmptyGenericWithBounds() throws IOException {
        ClassFile cf = new ClassFile(EmptyGenericWithBounds.class);
        Set<String> dependentClasses = cf.getDependentClasses();
        assertThat(dependentClasses).containsOnly(
                "java.lang.Object",
                "java.nio.Buffer");
    }

    @Test
    public void testGenericWithoutBounds() throws IOException {
        ClassFile cf = new ClassFile(GenericWithoutBounds.class);
        Set<String> dependentClasses = cf.getDependentClasses();
        assertThat(dependentClasses).containsOnly(
                "org.dessertj.samples.basic.Something",
                "java.lang.Object");
    }

    @Test
    public void testGenericWithoutBoundsEmpty() throws IOException {
        ClassFile cf = new ClassFile(GenericWithoutBoundsEmpty.class);
        Set<String> dependentClasses = cf.getDependentClasses();
        assertThat(dependentClasses).containsOnly(
                "org.dessertj.samples.basic.Something",
                "java.lang.Object");
    }

}
