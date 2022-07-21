package de.spricom.dessert.modules.core;

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
import de.spricom.dessert.slicing.Slice;

/**
 * Delegete for any {@link ModuleSlice} to be used for ModuleSlice extensions similar to
 * {@link de.spricom.dessert.slicing.AbstractDelegateSlice}.
 */
public class DelegateModule extends AbstractModule {

    private final ModuleSlice delegate;

    public DelegateModule(ModuleSlice delegate) {
        this.delegate = delegate;
    }

    @Override
    public final ModuleSlice getDelegate() {
        return delegate;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public String getVersion() {
        return delegate.getVersion();
    }

    @Override
    public Slice getUnqualifiedExports() {
        return delegate.getUnqualifiedExports();
    }

    @Override
    public Slice getImplementation() {
        return delegate.getImplementation();
    }
}
