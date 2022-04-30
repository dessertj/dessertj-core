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

import de.spricom.dessert.modules.core.ModuleLookup;
import de.spricom.dessert.modules.core.ModuleResolver;
import de.spricom.dessert.modules.core.ModuleSlice;
import de.spricom.dessert.modules.java.JavaModulesResolver;
import de.spricom.dessert.modules.jdk.JdkModulesResolver;
import de.spricom.dessert.modules.jpms.JavaPlatformModuleResolver;
import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.slicing.Classpath;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ModuleRegistry implements ModuleResolver, ModuleLookup {
    private static final Logger log = Logger.getLogger(ModuleRegistry.class.getName());

    private final Map<String, ModuleSlice> modules = new HashMap<String, ModuleSlice>();

    public ModuleRegistry(ModuleResolver... resolvers) {
        for (ModuleResolver resolver : resolvers) {
            addAll(resolver);
        }
    }

    public ModuleRegistry(Classpath cp) {
        this(defaultResolvers(cp));
    }

    private static ModuleResolver[] defaultResolvers(Classpath cp) {
        if (isJavaPlattformModuleSystemAvailable())  {
            return new ModuleResolver[] {
                new JavaPlatformModuleResolver(cp)
            };
        } else {
            return new ModuleResolver[] {
                    new JavaPlatformModuleResolver(cp),
                    new JavaModulesResolver(cp),
                    new JdkModulesResolver(cp)
            };
        }
    }

    public static boolean isJavaPlattformModuleSystemAvailable() {
        return ClassResolver.isJrtFileSystemAvailable();
    }

    public Collection<ModuleSlice> getModules() {
        return getModuleMap().values();
    }

    public Collection<String> getModuleNames() {
        return getModuleMap().keySet();
    }

    private Map<String, ModuleSlice> getModuleMap() {
        return Collections.unmodifiableMap(modules);
    }

    public ModuleSlice getModule(String name) {
        return modules.get(name);
    }

    private boolean add(ModuleSlice module) {
        String name = module.getName();
        ModuleSlice previous = modules.get(name);
        if (previous == null) {
            modules.put(name, module);
            return true;
        } else {
            if (!previous.equals(module)) {
                log.log(Level.WARNING, "There are two modules named '{}'", name);
            }
            return false;
        }
    }

    private boolean addAll(ModuleResolver resolver) {
        boolean modified = false;
        for (ModuleSlice module : resolver.getModules()) {
            modified |= add(module);
        }
        return modified;
    }
}
