package de.spricom.dessert.modules.jdk;

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
import de.spricom.dessert.modules.core.FixedModule;
import de.spricom.dessert.slicing.Classpath;
import de.spricom.dessert.slicing.Slices;

/**
 * Generated by de.spricom.dessert.tools.GenerateStaticModulesTool.
 */
class SecurityAuthModule extends FixedModule {

    SecurityAuthModule(Classpath cp) {
        super("jdk.security.auth", "17",
                Slices.of(
                        cp.slice("com.sun.security.auth.*"),
                        cp.slice("com.sun.security.auth.callback.*"),
                        cp.slice("com.sun.security.auth.login.*"),
                        cp.slice("com.sun.security.auth.module.*")
                ),
                Slices.of(
                        cp.slice("com.sun.security.auth.*"),
                        cp.slice("com.sun.security.auth.callback.*"),
                        cp.slice("com.sun.security.auth.login.*"),
                        cp.slice("com.sun.security.auth.module.*")
                ));
    }
}
