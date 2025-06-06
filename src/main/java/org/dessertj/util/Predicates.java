package org.dessertj.util;

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

public final class Predicates {
    private Predicates() {}

    public static <T> Predicate<T> any() {
        return new Predicate<T>() {
            @Override
            public boolean test(T t) {
                return true;
            }
        };
    }

    public static <T> Predicate<T> none() {
        return new Predicate<T>() {
            @Override
            public boolean test(T t) {
                return false;
            }
        };
    }

    public static <T> Predicate<T> and(final Predicate<T>... predicates) {
        return new Predicate<T>() {
            @Override
            public boolean test(T t) {
                for (Predicate<T> predicate : predicates) {
                    if (!predicate.test(t)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static <T> Predicate<T> or(final Predicate<T>... predicates) {
        return new Predicate<T>() {
            @Override
            public boolean test(T t) {
                for (Predicate<T> predicate : predicates) {
                    if (predicate.test(t)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static <T> Predicate<T> not(final Predicate<T> predicate) {
        return new Predicate<T>() {
            @Override
            public boolean test(T t) {
                return !predicate.test(t);
            }
        };
    }
}
