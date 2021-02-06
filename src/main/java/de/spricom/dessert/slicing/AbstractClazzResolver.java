package de.spricom.dessert.slicing;

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

import de.spricom.dessert.resolve.ClassEntry;

import java.util.HashSet;
import java.util.Set;

abstract class AbstractClazzResolver implements ClazzResolver {
    private final Classpath classpath;

    private Set<Clazz> clazzes;

    public AbstractClazzResolver(Classpath classpath) {
        this.classpath = classpath;
    }

    protected abstract void resolve();

    protected final void add(ClassEntry ce) {
        clazzes.add(classpath.asClazz(ce));
    }

    public Set<Clazz> getClazzes() {
        if (clazzes == null) {
            clazzes = new HashSet<Clazz>();
            resolve();
        }
        return clazzes;
    }

    public Classpath getClasspath() {
        return classpath;
    }
}
