package de.spricom.dessert.modules;

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

import de.spricom.dessert.slicing.Root;
import de.spricom.dessert.slicing.Slice;

/**
 * A root module is a combination of a {@link de.spricom.dessert.slicing.Root}
 * and a {@link Module}. It contains all classes that belong to the root.
 */
public class RootModule extends AbstractModule {
    private final String name;
    private final Root root;
    private final Slice moduleInterface;

    public RootModule(String name, Root root, Slice moduleInterface) {
        this.root = root;
        this.name = name;
        this.moduleInterface = moduleInterface;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Slice getInterface() {
        return moduleInterface;
    }

    @Override
    public Root getImplementation() {
        return root;
    }
}
