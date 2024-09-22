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
class LocaledataModule extends FixedModule {

    LocaledataModule(Classpath cp) {
        super("jdk.localedata", "21",
                Slices.of(
                        
                ),
                Slices.of(
                        cp.slice("sun.text.resources.cldr.ext.*"),
                        cp.slice("sun.text.resources.ext.*"),
                        cp.slice("sun.util.resources.cldr.ext.*"),
                        cp.slice("sun.util.resources.cldr.provider.*"),
                        cp.slice("sun.util.resources.ext.*"),
                        cp.slice("sun.util.resources.provider.*")
                ));
    }
}
