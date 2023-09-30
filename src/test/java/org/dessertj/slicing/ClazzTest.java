package org.dessertj.slicing;

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

import org.dessertj.samples.basic.Outer;
import org.dessertj.samples.dollar.Dollar;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class ClazzTest {
    private static final Classpath cp = new Classpath();

    @Test
    public void testThisClass() throws MalformedURLException {
        Slice slice = cp.slice(ClazzTest.class.getName());
        Set<Clazz> entries = slice.getClazzes();
        assertThat(entries).hasSize(1);
        Clazz entry = entries.iterator().next();
        assertThat(entry.getAlternatives()).hasSize(1);

        assertThat(entry.getName()).isEqualTo(getClass().getName());
        assertThat(entry.getSimpleName()).isEqualTo(getClass().getSimpleName());
        assertThat(entry.getClassFile().getThisClass()).isEqualTo(getClass().getName());
        assertThat(entry.getClassImpl()).isSameAs(getClass());
        assertThat(entry.getPackageName()).isEqualTo(getClass().getPackage().getName());

        assertThat(entry.getSuperclass().getClassImpl()).isSameAs(Object.class);
        assertThat(entry.getImplementedInterfaces()).isEmpty();
        assertThat(entry.getURI().toURL()).isEqualTo(getClass().getResource(getClass().getSimpleName() + ".class"));
        assertThat(new File(entry.getURI().toURL().getPath()).getAbsolutePath())
                .startsWith(entry.getRoot().getRootFile().getAbsolutePath());
    }

    @Test
    public void testInnerClass() {
        checkName(Outer.InnerIfc.class);
    }

    @Test
    public void testAnonymousInnerClass() {
        Outer outer = new Outer();
        Outer.InnerIfc anonymous = outer.useAnonymous();
        checkName(anonymous.getClass());
    }

    private void checkName(Class<?> classImpl) {
        Clazz clazz = cp.sliceOf(classImpl).getClazzes().iterator().next();

        System.out.println("Checking " + clazz.getName());
        assertThat(clazz.getName()).isEqualTo(classImpl.getName());
        assertThat(clazz.getSimpleName()).isEqualTo(classImpl.getSimpleName());
    }

    @Test
    public void testGetClassImpl() {
        Slice slice = cp.packageOf(Slice.class);
        for (Clazz clazz : slice.getClazzes()) {
            assertThat(clazz.getName()).isEqualTo(clazz.getClassImpl().getName());
        }
    }

    @Test
    public void testDessertNames() {
        Slice dessertj = cp.packageTreeOf("org.dessertj");
        for (Clazz clazz : dessertj.getClazzes()) {
            String name = clazz.getClassImpl().getName();
            assertThat(clazz.getName()).as(name).isEqualTo(name);
            assertThat(clazz.getPackageName() + "." + clazz.getShortName()).as(name).isEqualTo(name);
            assertThat(clazz.getSimpleName()).as(name).isEqualTo(clazz.getClassImpl().getSimpleName());
        }
        assertThat(dessertj.contains(cp.asClazz(Clazz.class)));
        assertThat(dessertj.contains(cp.asClazz(Dollar.class)));
    }
}
