package org.dessertj.util;

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
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.fest.assertions.Assertions.assertThat;


public class CombinationUtilsTest {

    @Test
    public void testFactorial() {
        assertThat(CombinationUtils.factorial(0)).isEqualTo(1);
        assertThat(CombinationUtils.factorial(1)).isEqualTo(1);
        assertThat(CombinationUtils.factorial(2)).isEqualTo(2);
        assertThat(CombinationUtils.factorial(3)).isEqualTo(6);
        assertThat(CombinationUtils.factorial(7)).isEqualTo(5040);
        assertThat(CombinationUtils.factorial(12)).isPositive();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactorialNegative() {
        CombinationUtils.factorial(-3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactorialToBig() {
        CombinationUtils.factorial(13);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPermutationListToSmall() {
        CombinationUtils.combinationsAsList(Collections.singletonList(1));
    }

    @Test
    public void testBinominal() {
        assertThat(CombinationUtils.binominal(2, 2)).isEqualTo(1);
        assertThat(CombinationUtils.binominal(7, 2)).isEqualTo(21);
        // assertThat(CombinationUtils.binominal(10, 2)).isEqualTo(CombinationUtils.combinationsSorted(10) * 2);
        Random rnd = new Random();
        for (int i = 1; i <= 1000; i++) {
            int n = rnd.nextInt(12) + 1;
            int k = rnd.nextInt(n) + 1;
            assertThat(CombinationUtils.binominal(n, k)).isEqualTo(binomial(n, k));
        }
    }

    private int binomial(int n, int k) {
        return CombinationUtils.factorial(n) / (CombinationUtils.factorial(k) * CombinationUtils.factorial(n - k));
    }

    @Test
    public void testCombinationsCount() {
        for (int i = 2; i < 13; i++) {
            assertThat(CombinationUtils.combinations(i))
                    .as("i = " + i)
                    .isEqualTo(CombinationUtils.factorial(i) / CombinationUtils.factorial(i - 2));
            assertThat(CombinationUtils.combinationsSorted(i))
                    .as("i = " + i)
                    .isEqualTo(binomial(i, 2));
        }
    }

    @Test
    public void testIndexes() {
        assertThat(CombinationUtils.indexes(5)).isEqualTo(Arrays.asList(0, 1, 2, 3, 4));
        assertThat(CombinationUtils.indexes(1)).isEqualTo(Collections.singletonList(0));
        assertThat(CombinationUtils.indexes(0)).isEmpty();
    }

    @Test
    public void testCombinations() {
        assertThat(asString(CombinationUtils.combinationsAsList(CombinationUtils.indexes(2))))
                .isEqualTo("[0,1],[1,0]");
        assertThat(asString(CombinationUtils.combinationsAsList(CombinationUtils.indexes(3))))
                .isEqualTo("[0,1],[1,0],[0,2],[2,0],[1,2],[2,1]");
        assertThat(asString(CombinationUtils.combinationsAsList(CombinationUtils.indexes(4))))
                .isEqualTo("[0,1],[1,0],[0,2],[2,0],[0,3],[3,0],[1,2],[2,1],[1,3],[3,1],[2,3],[3,2]");
        assertThat(CombinationUtils.combinationsAsList(CombinationUtils.indexes(7)))
                .hasSize(CombinationUtils.combinations(7));
    }

    @Test
    public void testCombinationsSorted() {
        assertThat(asString(CombinationUtils.combinationsSortedAsList(CombinationUtils.indexes(2))))
                .isEqualTo("[0,1]");
        assertThat(asString(CombinationUtils.combinationsSortedAsList(CombinationUtils.indexes(3))))
                .isEqualTo("[0,1],[0,2],[1,2]");
        assertThat(asString(CombinationUtils.combinationsSortedAsList(CombinationUtils.indexes(4))))
                .isEqualTo("[0,1],[0,2],[0,3],[1,2],[1,3],[2,3]");
        assertThat(CombinationUtils.combinationsSortedAsList(CombinationUtils.indexes(7)))
                .hasSize(CombinationUtils.combinationsSorted(7));
    }

    private String asString(List<Pair<Integer>> permutations) {
        StringBuilder sb = new StringBuilder(permutations.size() * 6);
        boolean first = true;
        for (Pair<Integer> permutation : permutations) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            sb.append("[").append(permutation.getLeft()).append(",").append(permutation.getRight()).append("]");
        }
        return sb.toString();
    }
}
