package de.spricom.dessert.util;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
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

import java.util.*;

public class CombinationUtils {

    public static <T> List<Pair<T>> combinationsAsList(final List<T> list) {
        return asList(combinations(list), combinations(list.size()));
    }

    public static <T> List<Pair<T>> combinationsSortedAsList(final List<T> list) {
        return asList(combinationsSorted(list), combinationsSorted(list.size()));
    }

    private static <T> List<Pair<T>> asList(Iterable<Pair<T>> iter, int capacity) {
        List<Pair<T>> results = new ArrayList<Pair<T>>(capacity);
        for (Pair<T> tPair : iter) {
            results.add(tPair);
        }
        return results;
    }

    public static <T> Iterable<Pair<T>> combinations(final List<T> list) {
        return new Iterable<Pair<T>>() {
            @Override
            public Iterator<Pair<T>> iterator() {
                return both(pairs(list));
            }
        };
    }

    public static <T> Iterable<Pair<T>> combinationsSorted(final List<T> list) {
        return new Iterable<Pair<T>>() {
            @Override
            public Iterator<Pair<T>> iterator() {
                return pairs(list);
            }
        };
    }

    public static List<Integer> indexes(int size) {
        Integer[] indexes = new Integer[size];
        for (int i = 0; i < size; i++) {
            indexes[i] = i;
        }
        return Arrays.asList(indexes);
    }

    private static <T> Iterator<Pair<T>> both(final Iterator<Pair<T>> pairs) {
        return new PermutationIterator<T>() {
            private Pair<T> current;

            @Override
            public boolean hasNext() {
                return current != null || pairs.hasNext();
            }

            @Override
            public Pair<T> next() {
                if (current == null) {
                    current = pairs.next();
                    return current;
                } else {
                    Pair<T> other = new Pair<T>(current.getRight(), current.getLeft());
                    current = null;
                    return other;
                }
            }
        };
    }

    private static <T> Iterator<Pair<T>> pairs(final List<T> list) {
        final int sz = list.size();
        if (sz < 2) {
            throw new IllegalArgumentException("A list must contain at least 2 elements to create a permutation.");
        }
        if (sz == 2) {
            return Collections.singleton(new Pair<T>(list.get(0), list.get(1))).iterator();
        }
        return new PermutationIterator<T>() {
            private final T first = list.get(0);
            private int index = 1;
            private Iterator<Pair<T>> rest;

            @Override
            public boolean hasNext() {
                return index < sz || rest().hasNext();
            }

            @Override
            public Pair<T> next() {
                if (index < sz) {
                    Pair<T> result = new Pair<T>(first, list.get(index));
                    index++;
                    return result;
                }
                return rest().next();
            }

            private Iterator<Pair<T>> rest() {
                if (rest == null) {
                    rest = pairs(list.subList(1, sz));
                }
                return rest;
            }
        };
    }

    public static int combinations(int n) {
        return combinationsSorted(n) * 2;
    }

    public static int combinationsSorted(int n) {
        return binominal(n, 2);
    }

    /**
     * Calculates @{code factorial(n) / (factorial(k) * factorial(n - k))}.
     *
     * @param n number of different values
     * @param k number of slots
     * @return number of possible combinations
     */
    public static int binominal(int n, int k) {
        if (k * 2 > n) {
            k = n - k;
        }
        int result = 1;
        for (int i = 1; i <= k; i++) {
            result = result * (n + 1 - i) / i;
        }
        return result;
    }

    public static int factorial(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial for negative numbers is not defined");
        }
        if (n <= 1) {
            return 1;
        }
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result = result * i;
            if (result > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Factorial of " + n + " does not fit into an integer.");
            }
        }
        return (int)result;
    }

    static abstract class PermutationIterator<T> implements Iterator<Pair<T>> {

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove elements from permutation.");
        }
    }
}
