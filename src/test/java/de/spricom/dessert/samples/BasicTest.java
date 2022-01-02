package de.spricom.dessert.samples;

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
import de.spricom.dessert.samples.basic.*;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

/**
 * This test has been generated by TestGeneratorTool.
 */
public class BasicTest {

    @Test
    public void testBar() throws IOException {
        ClassFile cf = new ClassFile(Bar.class);
        Set<String> dependentClasses = cf.getDependentClasses();
        assertThat(dependentClasses).containsOnly(
                "java.lang.Object");
    }

    @Test
    public void testBaz() throws IOException {
        ClassFile cf = new ClassFile(Baz.class);
        Set<String> dependentClasses = cf.getDependentClasses();
        assertThat(dependentClasses).containsOnly(
                "java.lang.Object");
    }

    @Test
    public void testFoo() throws IOException {
        ClassFile cf = new ClassFile(Foo.class);
        Set<String> dependentClasses = cf.getDependentClasses();
        assertThat(dependentClasses).containsOnly(
                "java.lang.Object");
    }

    @Test
    public void testLiterals() throws IOException {
        ClassFile cf = new ClassFile(Literals.class);
        Set<String> dependentClasses = cf.getDependentClasses();
        assertThat(dependentClasses).containsOnly(
                "de.spricom.dessert.samples.basic.Bar",
                "de.spricom.dessert.samples.basic.Baz",
                "de.spricom.dessert.samples.basic.Foo",
                "java.io.PrintStream",
                "java.lang.Class",
                "java.lang.Object",
                "java.lang.String",
                "java.lang.System");
    }

    @Test
    public void testOuter_1() throws IOException {
        ClassFile cf = new ClassFile(Outer.class.getResourceAsStream("Outer$1.class"));
        Set<String> dependentClasses = cf.getDependentClasses();
        assertThat(dependentClasses).containsOnly(
                "de.spricom.dessert.samples.basic.Outer",
                "de.spricom.dessert.samples.basic.Outer$InnerIfc",
                "java.lang.Object",
                "java.lang.String");
    }

    @Test
    public void testOuter_Inner() throws IOException {
        ClassFile cf = new ClassFile(Outer.class.getResourceAsStream("Outer$Inner.class"));
        Set<String> dependentClasses = cf.getDependentClasses();
        assertThat(dependentClasses).containsOnly(
                "de.spricom.dessert.samples.basic.Outer",
                "de.spricom.dessert.samples.basic.Outer$1",
                "de.spricom.dessert.samples.basic.Outer$InnerIfc",
                "java.lang.Object",
                "java.lang.String");
    }

    @Test
    public void testOuter_InnerIfc() throws IOException {
        ClassFile cf = new ClassFile(Outer.class.getResourceAsStream("Outer$InnerIfc.class"));
        Set<String> dependentClasses = cf.getDependentClasses();
        assertThat(dependentClasses).containsOnly(
                "de.spricom.dessert.samples.basic.Outer",
                "java.lang.Object",
                "java.lang.String");
    }

    @Test
    public void testOuter() throws IOException {
        ClassFile cf = new ClassFile(Outer.class);
        Set<String> dependentClasses = cf.getDependentClasses();
        assertThat(dependentClasses).containsOnly(
                "de.spricom.dessert.samples.basic.Outer$1",
                "de.spricom.dessert.samples.basic.Outer$Inner",
                "de.spricom.dessert.samples.basic.Outer$InnerIfc",
                "java.lang.Object");
    }

    @Test
    public void testSomething() throws IOException {
        ClassFile cf = new ClassFile(Something.class);
        Set<String> dependentClasses = cf.getDependentClasses();
        assertThat(dependentClasses).containsOnly(
                "java.lang.Object");
    }

}
