package de.spricom.dessert.modules.jpms;

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

import de.spricom.dessert.modules.core.ModuleResolver;
import de.spricom.dessert.slicing.Classpath;
import de.spricom.dessert.slicing.Clazz;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A {@link ModuleResolver} that scans the class-path for module-info classes.
 */
public final class JavaPlatformModuleResolver implements ModuleResolver {

    private final Classpath cp;

    public JavaPlatformModuleResolver(Classpath cp) {
        this.cp = cp;
    }

    @Override
    public List<JpmsModule> getModules() {
        Set<Clazz> moduleClazzes = cp.slice("module-info").getClazzes();
        List<JpmsModule> modules = new ArrayList<JpmsModule>(moduleClazzes.size());
        for (Clazz clazz : moduleClazzes) {
            JpmsModule module = new JpmsModule(clazz.getRoot(), clazz.getClassFile());
            modules.add(module);
        }
        return modules;
    }
}
