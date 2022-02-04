package de.spricom.dessert.modules.java;

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

class RmiModule extends FixedModule {

    RmiModule(Classpath cp) {
        super("java.rmi", "17",
                Slices.of(
                        cp.slice("java.rmi.*"),
                        cp.slice("java.rmi.dgc.*"),
                        cp.slice("java.rmi.registry.*"),
                        cp.slice("java.rmi.server.*"),
                        cp.slice("javax.rmi.ssl.*")
                ),
                Slices.of(
                        cp.slice("java.rmi.*"),
                        cp.slice("java.rmi.dgc.*"),
                        cp.slice("java.rmi.registry.*"),
                        cp.slice("java.rmi.server.*"),
                        cp.slice("javax.rmi.ssl.*"),
                        cp.slice("sun.rmi.log.*"),
                        cp.slice("sun.rmi.registry.*"),
                        cp.slice("sun.rmi.registry.resources.*"),
                        cp.slice("sun.rmi.runtime.*"),
                        cp.slice("sun.rmi.server.*"),
                        cp.slice("sun.rmi.transport.*"),
                        cp.slice("sun.rmi.transport.tcp.*")
                ));
    }
}
