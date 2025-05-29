package org.dessertj.samples.generics;

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

import org.dessertj.samples.basic.Something;

/**
 * According to 'jdeps' this class does have a dependency to java.lang.Object.
 * This is inconsistent with {@link GenericWithoutBoundsEmpty} without that
 * dependency.
 * DessertJ always adds the bounds to the dependencies, even if they are not
 * specified explicitly, hence they are java.lang.Object.
 *
 * @param <E> any generic
 */
public class GenericWithoutBounds<E> extends Something {
    private E anObject;

    public E getAnObject() {
        return anObject;
    }

    public void setAnObject(E anObject) {
        this.anObject = anObject;
    }

    public <F> F convert(E anObject) {
        return (F)anObject;
    }
}
