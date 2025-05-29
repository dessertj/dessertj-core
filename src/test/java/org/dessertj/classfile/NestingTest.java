package org.dessertj.classfile;

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
import org.dessertj.samples.basic.Bar;
import org.dessertj.samples.dollar.Dollar;
import org.dessertj.samples.dollar.Dollar$A$B$1;
import org.dessertj.samples.nesting.Host;
import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

public class NestingTest {

    @Test
    public void testThis() throws IOException {
        ClassFile cf = new ClassFile(getClass());
        System.out.printf(cf.dump());
        assertThat(cf.getSimpleName()).isEqualTo(getClass().getSimpleName());
        assertThat(cf.isInnerClass()).isFalse();
        assertThat(cf.isNestHost()).isFalse();
        assertThat(cf.getNestHost()).isNull();
        assertThat(cf.getNestMembers()).isEmpty();
    }

    @Test
    public void testBar() throws IOException {
        ClassFile cf = new ClassFile(Bar.class);
        System.out.printf(cf.dump());
        assertThat(cf.getSimpleName()).isEqualTo("Bar");
        assertThat(cf.isInnerClass()).isFalse();
        assertThat(cf.isNestHost()).isFalse();
        assertThat(cf.getNestHost()).isNull();
        assertThat(cf.getNestMembers()).isEmpty();
    }

    @Test
    public void testHost() throws IOException {
        ClassFile cf = new ClassFile(Host.class);
        System.out.printf(cf.dump());
        assertThat(cf.getSimpleName()).isEqualTo("Host");
        assertThat(cf.isInnerClass()).isFalse();
        assertThat(cf.isNestHost()).isTrue();
        assertThat(cf.getNestHost()).isEqualTo(Host.class.getName());
        assertThat(cf.getNestMembers()).hasSize(12);
    }

    @Test
    public void testPublicStaticHost() throws IOException {
        ClassFile cf = new ClassFile(Host.PublicStatic.class);
        System.out.printf(cf.dump());
        assertThat(cf.getSimpleName()).isEqualTo("PublicStatic");
        assertThat(cf.isInnerClass()).isTrue();
        assertThat(cf.isNestHost()).isFalse();
        assertThat(cf.getNestHost()).isEqualTo(Host.class.getName());
        assertThat(cf.getNestMembers()).hasSize(2)
                .contains(Host.class.getName() + "$PublicStatic");
    }

    @Test
    public void testNestedDefaultStatic() throws IOException {
        String name = "$PublicStatic$NestedDefaultStatic";
        ClassFile cf = new ClassFile(Host.class.getResourceAsStream("Host" + name + ".class"));
        System.out.printf(cf.dump());
        assertThat(cf.getSimpleName()).isEqualTo("NestedDefaultStatic");
        assertThat(cf.isInnerClass()).isTrue();
        assertThat(cf.isNestHost()).isFalse();
        assertThat(cf.getNestHost()).isEqualTo(Host.class.getName());
        assertThat(cf.getNestMembers()).hasSize(2).contains(Host.class.getName() + name);
    }

    @Test
    public void testNestedPrivateStatic() throws IOException {
        String name = "$PublicStatic$NestedDefaultStatic$NestedPrivateStatic";
        ClassFile cf = new ClassFile(Host.class.getResourceAsStream("Host" + name + ".class"));
        System.out.printf(cf.dump());
        assertThat(cf.getSimpleName()).isEqualTo("NestedPrivateStatic");
        assertThat(cf.isInnerClass()).isTrue();
        assertThat(cf.isNestHost()).isFalse();
        assertThat(cf.getNestHost()).isEqualTo(Host.class.getName());
        assertThat(cf.getNestMembers()).hasSize(1).contains(Host.class.getName() + name);
    }

    @Test
    public void testNestedDefault() throws IOException {
        String name = "$DefaultClass";
        ClassFile cf = new ClassFile(Host.class.getResourceAsStream("Host" + name + ".class"));
        System.out.printf(cf.dump());
        assertThat(cf.getSimpleName()).isEqualTo("DefaultClass");
        assertThat(cf.isInnerClass()).isTrue();
        assertThat(cf.isNestHost()).isFalse();
        assertThat(cf.getNestHost()).isEqualTo(Host.class.getName());
        assertThat(cf.getNestMembers()).hasSize(2).contains(Host.class.getName() + name);
    }

    @Test
    public void testNestedDefaultAnonymous() throws IOException {
        String name = "$DefaultClass$1";
        ClassFile cf = new ClassFile(Host.class.getResourceAsStream("Host" + name + ".class"));
        System.out.printf(cf.dump());
        assertThat(cf.getSimpleName()).isEqualTo("");
        assertThat(cf.isInnerClass()).isTrue();
        assertThat(cf.isNestHost()).isFalse();
        assertThat(cf.getNestHost()).isEqualTo(Host.class.getName());
        assertThat(cf.getNestMembers()).hasSize(1).contains(Host.class.getName() + name);
    }

    @Test
    public void testDollar() throws IOException {
        ClassFile cf = new ClassFile(Dollar.class);
        System.out.printf(cf.dump());
        assertThat(cf.getSimpleName()).isEqualTo("Dollar");
        assertThat(cf.isInnerClass()).isFalse();
        assertThat(cf.isNestHost()).isTrue();
        assertThat(cf.getNestHost()).isEqualTo(Dollar.class.getName());
        assertThat(cf.getNestMembers()).hasSize(1);
    }

    @Test
    public void testDollarA() throws IOException {
        ClassFile cf = new ClassFile(Dollar.A.class);
        System.out.printf(cf.dump());
        assertThat(cf.getSimpleName()).isEqualTo("A");
        assertThat(cf.isInnerClass()).isTrue();
        assertThat(cf.isNestHost()).isFalse();
        assertThat(cf.getNestHost()).isEqualTo(Dollar.class.getName());
        assertThat(cf.getNestMembers()).hasSize(2);
    }

    @Test
    public void testDollarAB() throws IOException {
        ClassFile cf = new ClassFile(Dollar.A.B.class);
        System.out.printf(cf.dump());
        assertThat(cf.getSimpleName()).isEqualTo("B");
        assertThat(cf.isInnerClass()).isTrue();
        assertThat(cf.isNestHost()).isFalse();
        assertThat(cf.getNestHost()).isEqualTo(Dollar.class.getName());
        assertThat(cf.getNestMembers()).hasSize(2);
    }

    @Test
    public void testDollarABAnonymous() throws IOException {
        ClassFile cf = new ClassFile(new Dollar.A.B().comparator.getClass());
        System.out.printf(cf.dump());
        assertThat(cf.getSimpleName()).isEqualTo("");
        assertThat(cf.isInnerClass()).isTrue();
        assertThat(cf.isNestHost()).isFalse();
        assertThat(cf.getNestHost()).isEqualTo(Dollar.class.getName());
        assertThat(cf.getNestMembers()).hasSize(1);
    }

    @Test
    public void testDollar$A$B$1() throws IOException {
        ClassFile cf = new ClassFile(Dollar$A$B$1.class);
        System.out.printf(cf.dump());
        assertThat(cf.getSimpleName()).isEqualTo("Dollar$A$B$1");
        assertThat(cf.isInnerClass()).isFalse();
        assertThat(cf.isNestHost()).isTrue();
        assertThat(cf.getNestHost()).isEqualTo(Dollar$A$B$1.class.getName());
        assertThat(cf.getNestMembers()).hasSize(1)
                .contains(Dollar$A$B$1.Nest$2.class.getName());
    }

    @Test
    public void testDollar$A$B$1Nest$2() throws IOException {
        ClassFile cf = new ClassFile(Dollar$A$B$1.Nest$2.class);
        System.out.printf(cf.dump());
        assertThat(cf.getSimpleName()).isEqualTo("Nest$2");
        assertThat(cf.isInnerClass()).isTrue();
        assertThat(cf.isNestHost()).isFalse();
        assertThat(cf.getNestHost()).isEqualTo(Dollar$A$B$1.class.getName());
        assertThat(cf.getNestMembers()).hasSize(2)
                .contains(Dollar$A$B$1.Nest$2.class.getName(), Dollar$A$B$1.Nest$2.Nest$Nest.class.getName());
    }

    @Test
    public void testDollar$A$B$1Nest$2Nest$Nest() throws IOException {
        ClassFile cf = new ClassFile(Dollar$A$B$1.Nest$2.Nest$Nest.class);
        System.out.printf(cf.dump());
        assertThat(cf.getSimpleName()).isEqualTo("Nest$Nest");
        assertThat(cf.isInnerClass()).isTrue();
        assertThat(cf.isNestHost()).isFalse();
        assertThat(cf.getNestHost()).isEqualTo(Dollar$A$B$1.class.getName());
        assertThat(cf.getNestMembers()).hasSize(1)
                .contains(Dollar$A$B$1.Nest$2.Nest$Nest.class.getName());
    }
}
