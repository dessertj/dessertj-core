package de.spricom.dessert.modules;

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

/**
 * Maintains a map of modules found on the class-path,
 * so that they can be accessed by <i>module-name</i>.
 */
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

    /**
     * Checks whether the JPMS is available. This is always true for Java 9 or later.
     *
     * @return true, if the current JDK is Java 9 or newer.
     */
    public static boolean isJavaPlattformModuleSystemAvailable() {
        return ClassResolver.isJrtFileSystemAvailable();
    }

    /**
     * Returns all module slices found on the class-path.
     *
     * @return the module slices
     */
    public Collection<ModuleSlice> getModules() {
        return getModuleMap().values();
    }

    /**
     * Returns the names of all modules found on the class-path.
     *
     * @return the names
     */
    public Collection<String> getModuleNames() {
        return getModuleMap().keySet();
    }

    private Map<String, ModuleSlice> getModuleMap() {
        return Collections.unmodifiableMap(modules);
    }

    /**
     * Return a module by name.
     *
     * @param name the module name
     * @return the module or null if there is no such module.
     */
    public ModuleSlice getModule(String name) {
        return modules.get(name);
    }

    // TODO: public ModuleSlice getModuleOf(Class<?> clazz)...

    private boolean add(ModuleSlice module) {
        String name = module.getName();
        ModuleSlice previous = modules.get(name);
        if (previous == null) {
            modules.put(name, module);
            return true;
        } else {
            if (!previous.equals(module)) {
                log.log(Level.WARNING, "There are two modules named ''{0}''", name);
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
