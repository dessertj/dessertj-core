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

/**
 * Functional interface to render a dependency assertion failure.
 */
public interface IllegalDependenciesRenderer {
    /**
     * Creates a string representation of assertion failure to
     * be used as message for an {@link AssertionError}.
     *
     * @param violations the dependency violations
     * @return the message
     */
    String render(IllegalDependencies violations);
}
