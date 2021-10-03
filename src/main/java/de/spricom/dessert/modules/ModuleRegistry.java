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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModuleRegistry {
    private Map<String, Module> modules = new HashMap<String, Module>();

    public Collection<String> getNames() {
        return getModuleMap().keySet();
    }

    public Collection<Module> getModules() {
        return getModuleMap().values();
    }

    public Map<String, Module> getModuleMap() {
        return Collections.unmodifiableMap(modules);
    }

    public Module getModule(String name) {
        return modules.get(name);
    }

    public void register(Module module) {
        String name = module.getName();
        Module previous = modules.get(name);
        if (previous == null) {
            modules.put(name, module);
        } else if (!previous.equals(module)) {
            throw new DuplicateModuleNameException("There are two modules named '" + name + "'");
        }
    }

    public void registerAll(ModuleRegistry otherRegistry) {
        for (Module module : otherRegistry.getModules()) {
            register(module);
        }
    }
}
