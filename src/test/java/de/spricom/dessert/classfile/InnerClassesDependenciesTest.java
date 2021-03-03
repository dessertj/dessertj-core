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

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.fest.assertions.Assertions.assertThat;

public class InnerClassesDependenciesTest {
    private int count;

    interface AnInnerInterface {
        void foo();
    }

    class AnInnerClass {
        void foo() throws IOException {
            testThis();
        }
    }

    public static class StaticInnerClass {
        void foo() {
        }
    }

    private Runnable createAnonymousInnerClass() {
        return new Runnable() {
            @Override
            public void run() {
            }
        };
    }

    private Runnable createAnonymousInnerClassWithFieldReference() {
        return new Runnable() {
            @Override
            public void run() {
                count++;
            }
        };
    }

    private Runnable createAnonymousInnerClassWithMethodReference() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    testThis();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    private Runnable createAnonymousInnerClassWithLocalReference() {
        final AtomicInteger counter = new AtomicInteger();
        return new Runnable() {
            @Override
            public void run() {
                counter.addAndGet(1);
            }
        };
    }

    @Test
    public void testThis() throws IOException {
        ClassFile cf = new ClassFile(getClass());
        assertThat(cf.getDependentClasses()).containsOnly(
                "de.spricom.dessert.classfile.ClassFile",
                "de.spricom.dessert.classfile.InnerClassesDependenciesTest$1",
                "de.spricom.dessert.classfile.InnerClassesDependenciesTest$2",
                "de.spricom.dessert.classfile.InnerClassesDependenciesTest$3",
                "de.spricom.dessert.classfile.InnerClassesDependenciesTest$4",
                "de.spricom.dessert.classfile.InnerClassesDependenciesTest$AnInnerClass",
                "de.spricom.dessert.classfile.InnerClassesDependenciesTest$AnInnerInterface",
                "de.spricom.dessert.classfile.InnerClassesDependenciesTest$StaticInnerClass",
                "java.io.IOException",
                "java.lang.Class",
                "java.lang.Object",
                "java.lang.Runnable",
                "java.util.concurrent.atomic.AtomicInteger",
                "java.util.Collection",
                "java.util.Set",
                "org.fest.assertions.Assertions",
                "org.fest.assertions.CollectionAssert",
                "org.junit.Test",
                "java.io.PrintStream",
                "java.lang.String",
                "java.lang.System"
        );
    }

    @Test
    public void testInnerInterface() throws IOException {
        ClassFile cf = new ClassFile(AnInnerInterface.class);
        System.out.println(cf.dump());
        assertThat(cf.getDependentClasses()).containsOnly(
                "de.spricom.dessert.classfile.InnerClassesDependenciesTest",
                "java.lang.Object"
        );
    }

    @Test
    public void testInnerClass() throws IOException {
        ClassFile cf = new ClassFile(AnInnerClass.class);
        assertThat(cf.getDependentClasses()).containsOnly(
                "de.spricom.dessert.classfile.InnerClassesDependenciesTest",
                "java.lang.Object",
                "java.io.IOException"
        );
    }

    @Test
    public void testStaticInnerClass() throws IOException {
        ClassFile cf = new ClassFile(StaticInnerClass.class);
        assertThat(cf.getDependentClasses()).containsOnly(
                "de.spricom.dessert.classfile.InnerClassesDependenciesTest",
                "java.lang.Object"
        );
    }

    @Test
    public void testAnonymousInnerClass() throws IOException {
        ClassFile cf = new ClassFile(createAnonymousInnerClass().getClass());
        assertThat(cf.getDependentClasses()).containsOnly(
                "de.spricom.dessert.classfile.InnerClassesDependenciesTest",
                "java.lang.Runnable",
                "java.lang.Object"
        );
    }

    @Test
    public void testAnonymousInnerClassWithFieldReference() throws IOException {
        ClassFile cf = new ClassFile(createAnonymousInnerClassWithFieldReference().getClass());
        assertThat(cf.getDependentClasses()).containsOnly(
                "de.spricom.dessert.classfile.InnerClassesDependenciesTest",
                "java.lang.Runnable",
                "java.lang.Object"
        );
    }

    @Test
    public void testAnonymousInnerClassWithMethodReference() throws IOException {
        ClassFile cf = new ClassFile(createAnonymousInnerClassWithMethodReference().getClass());
        assertThat(cf.getDependentClasses()).containsOnly(
                "de.spricom.dessert.classfile.InnerClassesDependenciesTest",
                "java.lang.Runnable",
                "java.lang.Object",
                "java.io.IOException"
        );
    }

    @Test
    public void testAnonymousInnerClassWithLocalReference() throws IOException {
        ClassFile cf = new ClassFile(createAnonymousInnerClassWithLocalReference().getClass());
        assertThat(cf.getDependentClasses()).containsOnly(
                "de.spricom.dessert.classfile.InnerClassesDependenciesTest",
                "java.lang.Runnable",
                "java.lang.Object",
                "java.util.concurrent.atomic.AtomicInteger"
        );
    }
}
