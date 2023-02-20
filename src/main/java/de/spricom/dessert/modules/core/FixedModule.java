package de.spricom.dessert.modules.core;

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
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.Slices;

/**
 * A fixed module is a hard-coded module definition.
 */
public class FixedModule extends AbstractModule {

    private final String name;
    private final String version;
    private final Slice unqualifiedExports;
    private final Slice implementation;

    public FixedModule(String name, String version, Slice unqualifiedExports, Slice implementation) {
        this.name = name;
        this.version = version;
        this.unqualifiedExports = unqualifiedExports;
        this.implementation = implementation;
    }

    public FixedModule(String name, String version) {
        this.name = name;
        this.version = version;
        this.unqualifiedExports = Slices.EMPTY_SLICE;
        this.implementation = Slices.EMPTY_SLICE;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Slice getUnqualifiedExports() {
        return unqualifiedExports;
    }

    @Override
    public Slice getImplementation() {
        return implementation;
    }
}
