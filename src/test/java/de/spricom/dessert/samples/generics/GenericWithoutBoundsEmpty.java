package de.spricom.dessert.samples.generics;

/*-
 * #%L
 * Dessert Dependency Assertion Library
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

import de.spricom.dessert.samples.basic.Something;

/**
 * According to 'jdeps' this class does not have a dependency to java.lang.Object.
 * This is inconsistent with {@link GenericWithoutBounds} without that
 * dependency.
 * Dessert always adds the bounds to the dependencies, even if they are not
 * sepecified explicity, hence they are java.lang.Object.
 *
 * @param <E> any object
 */
public class GenericWithoutBoundsEmpty<E> extends Something {
}
