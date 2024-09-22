package org.dessertj.modules.jdk;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2024 Hans Jörg Heßmann
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

import org.dessertj.modules.core.FixedModule;
import org.dessertj.slicing.Classpath;
import org.dessertj.slicing.Slices;

/**
 * Generated by org.dessertj.tools.GenerateStaticModulesTool.
 */
class DynalinkModule extends FixedModule {

    DynalinkModule(Classpath cp) {
        super("jdk.dynalink", "21",
                Slices.of(
                        cp.slice("jdk.dynalink.*"),
                        cp.slice("jdk.dynalink.beans.*"),
                        cp.slice("jdk.dynalink.linker.*"),
                        cp.slice("jdk.dynalink.linker.support.*"),
                        cp.slice("jdk.dynalink.support.*")
                ),
                Slices.of(
                        cp.slice("jdk.dynalink.*"),
                        cp.slice("jdk.dynalink.beans.*"),
                        cp.slice("jdk.dynalink.internal.*"),
                        cp.slice("jdk.dynalink.linker.*"),
                        cp.slice("jdk.dynalink.linker.support.*"),
                        cp.slice("jdk.dynalink.support.*")
                ));
    }
}
