package org.dessertj.partitioning;

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

import org.dessertj.slicing.Classpath;
import org.dessertj.slicing.Clazz;
import org.dessertj.slicing.Slice;
import org.dessertj.util.ClassUtils;
import org.dessertj.util.Predicate;
import org.dessertj.util.Predicates;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class PartitioningTest {
    private static final int SLICING_COUNT = 29;
    private static final int PUBLIC_COUNT = 14;
    private static final int INTERFACE_COUNT = 5;
    private static final int FINAL_COUNT = 8;
    private static final int INNER_TYPE_COUNT = 8;

    private static final Classpath cp = new Classpath();

    private Slice slicing;

    private boolean containsPackageInfo;

    @Before
    public void init() {
        slicing = cp.rootOf(Slice.class).packageOf(Slice.class).named("slicing");
        containsPackageInfo = !slicing
                .slice(ClazzPredicates.SYNTHETIC)
                .slice("..package-info")
                .getClazzes()
                .isEmpty();
    }

    private int slicingCount() {
        return SLICING_COUNT + (containsPackageInfo ? 1 : 0);
    }

    @Test
    public void testEach() {
        assertThat(slicing.slice(ClazzPredicates.EACH).getClazzes()).hasSize(slicingCount());
    }

    @Test
    public void testPublic() {
        assertThat(slicing.slice(ClazzPredicates.PUBLIC).getClazzes()).hasSize(PUBLIC_COUNT);
        Predicate<Class<?>> isPublic = new Predicate<Class<?>>() {
            @Override
            public boolean test(Class<?> clazz) {
                return ClassUtils.isPublic(clazz);
            }
        };
        assertThat(slicing.slice(ClazzPredicates.matches(isPublic)).getClazzes()).hasSize(PUBLIC_COUNT);
    }

    @Test
    public void testInterface() {
        Predicate<Clazz> nonSyntheticInterface =
                Predicates.and(ClazzPredicates.INTERFACE, Predicates.not(ClazzPredicates.SYNTHETIC));
        assertThat(slicing.slice(nonSyntheticInterface).getClazzes())
                .hasSize(INTERFACE_COUNT);
    }

    @Test
    public void testFinal() {
        assertThat(slicing.slice(ClazzPredicates.FINAL).getClazzes()).hasSize(FINAL_COUNT);
    }

    @Test
    public void testInner() {
        assertThat(slicing.slice(ClazzPredicates.INNER_TYPE).getClazzes()).hasSize(INNER_TYPE_COUNT);
        Predicate<Class<?>> isInnerType = new Predicate<Class<?>>() {
            @Override
            public boolean test(Class<?> clazz) {
                return ClassUtils.isInnerType(clazz);
            }
        };
        assertThat(slicing.slice(ClazzPredicates.matches(isInnerType)).getClazzes()).hasSize(INNER_TYPE_COUNT);
    }

    @Test
    public void testNotFinal() {
        assertThat(slicing.slice(Predicates.not(ClazzPredicates.FINAL)).getClazzes())
                .hasSize(slicingCount() - FINAL_COUNT);
    }

    @Test
    public void testAnd() {
        assertThat(slicing.slice(ClazzPredicates.PUBLIC).slice(slicing.slice(ClazzPredicates.FINAL)).getClazzes())
                .hasSize(3);
        assertThat(slicing.slice(Predicates.and(ClazzPredicates.FINAL, ClazzPredicates.PUBLIC)).getClazzes())
                .hasSize(3);
    }

    @Test
    public void testOr() {
        assertThat(slicing.slice(ClazzPredicates.PUBLIC).plus(slicing.slice(ClazzPredicates.FINAL)).getClazzes())
                .hasSize(19);
        assertThat(slicing.slice(Predicates.or(ClazzPredicates.FINAL, ClazzPredicates.PUBLIC)).getClazzes())
                .hasSize(19);
    }
}
