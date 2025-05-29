package org.dessertj.slicing;

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

import org.dessertj.partitioning.ClazzPredicates;
import org.dessertj.util.Predicates;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class SliceTest {
    private static final Classpath cp = new Classpath();

    private Slice slicing;
    private Slice publics;
    private Slice nonPublics;

    @Before
    public void init() {
        slicing = cp.rootOf(Slice.class).packageOf(Slice.class).named("slicing");
        publics = slicing.slice(ClazzPredicates.PUBLIC).named("publics");
        nonPublics = slicing.slice(Predicates.not(ClazzPredicates.PUBLIC)).named("nonPublics");
    }

    @Test
    public void testSlices() {
        assertThat(slicing.toString()).isEqualTo("slicing");
        assertThat(publics.getClazzes().size() + nonPublics.getClazzes().size())
                .isEqualTo(slicing.getClazzes().size());
    }

    @Test
    public void testPlus() {
        assertThat(publics.plus(nonPublics).getClazzes()).isEqualTo(slicing.getClazzes());
        assertThat(nonPublics.plus(publics.getClazzes()).getClazzes()).isEqualTo(slicing.getClazzes());
    }

    @Test
    public void testMinus() {
        assertThat(slicing.minus(publics).getClazzes()).isEqualTo(nonPublics.getClazzes());
        assertThat(slicing.minus(publics.getClazzes()).getClazzes()).isEqualTo(nonPublics.getClazzes());
    }

    @Test
    public void testSlice() {
        assertThat(slicing.slice(publics).getClazzes()).isEqualTo(publics.getClazzes());
        assertThat(slicing.slice(publics.getClazzes()).getClazzes()).isEqualTo(publics.getClazzes());
        assertThat(publics.slice(nonPublics).getClazzes()).isEmpty();
        assertThat(publics.slice(nonPublics.getClazzes()).getClazzes()).isEmpty();
    }
}
