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

import de.spricom.dessert.util.Sets;

import java.util.*;

public class CompositeModuleResolver implements ModuleResolver {
    private final List<ModuleResolver> resolvers;
    private final Map<String, Module> modules;

    private CompositeModuleResolver(List<ModuleResolver> resolvers) {
        this.resolvers = resolvers;
        this.modules = new HashMap<String, Module>();
        for (ModuleResolver resolver : resolvers) {
            Map<String, Module> map = resolver.getModules();
            Set<String> duplicates = Sets.intersection(map.keySet(), modules.keySet());
            if (!duplicates.isEmpty()) {
                // TODO: More information could be added to the exception, i.e. the jars involved
                throw new DuplicateModuleNameException("Module names are not unique: " + duplicates);
            }
            modules.putAll(map);
        }
    }

    public static CompositeModuleResolver of(ModuleResolver... resolvers) {
        return new CompositeModuleResolver(Arrays.asList(resolvers));
    }

    @Override
    public Module getModule(String name) {
        return modules.get(name);
    }

    @Override
    public Collection<String> getModuleNames() {
        return modules.keySet();
    }

    @Override
    public Map<String, Module> getModules() {
        return modules;
    }
}
