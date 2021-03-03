package de.spricom.dessert.classfile;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
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

import de.spricom.dessert.samples.annotations.SpecialArgSample;
import de.spricom.dessert.samples.generics.EmptyGenericWithBounds;
import de.spricom.dessert.samples.generics.GenericWithoutBounds;
import de.spricom.dessert.samples.generics.GenericWithoutBoundsEmpty;
import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

public class SamplesDependenciesTest {

    /**
     * Make sure dependencies introduced by a
     * <a href="https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.7.18">RuntimeVisibleParameterAnnotations Attribute</a>
     * are detected.
     */
    @Test
    public void testAnnotation() throws IOException {
        ClassFile cf = new ClassFile(SpecialArgSample.class);
        assertThat(cf.getDependentClasses()).containsOnly(
                "de.spricom.dessert.samples.annotations.SpecialArg",
                "java.io.PrintStream",
                "java.lang.Object",
                "java.lang.String",
                "java.lang.StringBuilder",
                "java.lang.System");
    }

    /**
     * Make sure the bounds of a generic are always added as a dependency, even if it
     * is java.lang.Object. <b>Dessert differs from jdeps, here.</b>
     */
    @Test
    public void testGenericWithoutBounds() throws IOException {
        ClassFile cf = new ClassFile(GenericWithoutBounds.class);
        assertThat(cf.getDependentClasses()).containsOnly(
                "java.lang.Object",
                "de.spricom.dessert.samples.basic.Something");
    }

    /**
     * Make sure implicit the bounds of a generic are always added as an dependency even
     * if there is no field or method using the type argument.
     * <b>Dessert differs from jdeps, here.</b>
     */
    @Test
    public void testGenericWithoutBoundsEmpty() throws IOException {
        ClassFile cf = new ClassFile(GenericWithoutBoundsEmpty.class);
        System.out.println(cf.dump());
        assertThat(cf.getDependentClasses()).containsOnly(
                "java.lang.Object",
                "de.spricom.dessert.samples.basic.Something");
    }

    /**
     * Make sure the explicit bounds of a generic are always added as an dependency even
     * if there is no field or method using the type argument.
     * <b>Dessert differs from jdeps, here.</b>
     */
    @Test
    public void testEmptyGenericWithBounds() throws IOException {
        ClassFile cf = new ClassFile(EmptyGenericWithBounds.class);
        assertThat(cf.getDependentClasses()).containsOnly(
                "java.lang.Object",
                "java.nio.Buffer");
    }
}
