package de.spricom.dessert.util;

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
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;


public class PermutationUtilsTest {

    @Test
    public void testFactorial() {
        assertThat(PermutationUtils.factorial(0)).isEqualTo(1);
        assertThat(PermutationUtils.factorial(1)).isEqualTo(1);
        assertThat(PermutationUtils.factorial(2)).isEqualTo(2);
        assertThat(PermutationUtils.factorial(3)).isEqualTo(6);
        assertThat(PermutationUtils.factorial(7)).isEqualTo(5040);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactorialNegative() {
        PermutationUtils.factorial(-3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactorialToBig() {
        PermutationUtils.factorial(42);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPermutationListToSmall() {
        PermutationUtils.permuteAsList(Collections.singletonList(1));
    }

    @Test
    public void testBinominal() {
        assertThat(PermutationUtils.binominal(2, 2)).isEqualTo(1);
        assertThat(PermutationUtils.binominal(7, 2)).isEqualTo(21);
        assertThat(PermutationUtils.binominal(10, 2)).isEqualTo(PermutationUtils.pairs(10) / 2);
    }

    @Test
    public void testPermuteAsList() {
        assertThat(asString(PermutationUtils.permuteAsList(Arrays.asList(1, 2)))).isEqualTo("[1,2],[2,1]");
        assertThat(asString(PermutationUtils.permuteAsList(Arrays.asList(1, 2, 3))))
                .isEqualTo("[1,2],[2,1],[1,3],[3,1],[2,3],[3,2]");
        assertThat(asString(PermutationUtils.permuteAsList(Arrays.asList(1, 2, 3, 4))))
                .isEqualTo("[1,2],[2,1],[1,3],[3,1],[1,4],[4,1],[2,3],[3,2],[2,4],[4,2],[3,4],[4,3]");
        assertThat(PermutationUtils.permuteAsList(Arrays.asList(1, 2, 3, 4, 5, 6, 7)))
                .hasSize(PermutationUtils.pairs(7));
    }

    private String asString(List<Pair<Integer>> permuations) {
        StringBuilder sb = new StringBuilder(permuations.size() * 6);
        boolean first = true;
        for (Pair<Integer> permuation : permuations) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            sb.append("[").append(permuation.getLeft()).append(",").append(permuation.getRight()).append("]");
        }
        return sb.toString();
    }
}
