package de.spricom.dessert.modules;

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

import de.spricom.dessert.modules.core.Module;
import de.spricom.dessert.modules.core.ModuleLookup;
import de.spricom.dessert.modules.core.ModuleResolver;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModuleRegistry implements ModuleResolver, ModuleLookup {
    private Logger log = Logger.getLogger(ModuleRegistry.class.getName());

    private Map<String, Module> modules = new HashMap<String, Module>();

    public ModuleRegistry(ModuleResolver... resolvers) {
        for (ModuleResolver resolver : resolvers) {
            addAll(resolver);
        }
    }

    public Collection<Module> getModules() {
        return getModuleMap().values();
    }

    public Collection<String> getModuleNames() {
        return getModuleMap().keySet();
    }

    private Map<String, Module> getModuleMap() {
        return Collections.unmodifiableMap(modules);
    }

    public Module getModule(String name) {
        return modules.get(name);
    }

    private boolean add(Module module) {
        String name = module.getName();
        Module previous = modules.get(name);
        if (previous == null) {
            modules.put(name, module);
            return true;
        } else {
            if (!previous.equals(module)) {
                log.log(Level.FINE, "There are two modules named '{}'", name);
            }
            return false;
        }
    }

    private boolean addAll(ModuleResolver resolver) {
        boolean modified = false;
        for (Module module : resolver.getModules()) {
            modified |= add(module);
        }
        return modified;
    }
}
