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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PermutationUtils {

    public static <T> List<Pair<T>> permuteAsList(final List<T> list) {
        List<Pair<T>> results = new ArrayList<Pair<T>>(pairs(list.size()));
        for (Pair<T> tPair : permute(list)) {
            results.add(tPair);
        }
        return results;
    }

    public static <T> Iterable<Pair<T>> permute(final List<T> list) {
        return new Iterable<Pair<T>>() {
            @Override
            public Iterator<Pair<T>> iterator() {
                return both(pairs(list));
            }
        };
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
            throw new IllegalArgumentException("A list must contain at least 2 elememts to create a permuation.");
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

    public static int pairs(int n) {
        return factorial(n) / factorial(n - 2);
    }

    public static int binominal(int n, int k) {
        return factorial(n) / (factorial(n - k) * factorial(k));
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
