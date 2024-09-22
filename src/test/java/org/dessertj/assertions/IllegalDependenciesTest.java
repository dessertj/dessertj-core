package org.dessertj.assertions;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2024 Hans Jörg Heßmann
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

import org.dessertj.slicing.Classpath;
import org.dessertj.slicing.Clazz;
import org.junit.Test;

import static org.dessertj.assertions.SliceAssertions.dessert;
import static org.fest.assertions.Assertions.assertThat;

public class IllegalDependenciesTest {

    @Test
    public void testDetectOneIllegalDependency() {
        Classpath cp = new Classpath();
        Clazz me = cp.asClazz(this.getClass());
        try {
            dessert(me).usesNot(cp.rootOf(Test.class));
        } catch (AssertionError er) {
            assertThat(er.getMessage()).isEqualTo("Illegal Dependencies:\n" +
                    "org.dessertj.assertions.IllegalDependenciesTest\n" +
                    " -> org.junit.Test\n");
        }
    }
}
