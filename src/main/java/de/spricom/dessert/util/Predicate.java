package de.spricom.dessert.util;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2021 Hans Jörg Heßmann
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

/**
 * A functional interface for a test on any object.
 *
 * @param <T> the type of object to test
 * @see Predicates for logic to combine predicates
 */
public interface Predicate<T> {

    /**
     * Test the predicate for some object
     *
     * @param t the object to test
     * @return return true if the predicate is fulfilled
     */
    boolean test(T t);
}
