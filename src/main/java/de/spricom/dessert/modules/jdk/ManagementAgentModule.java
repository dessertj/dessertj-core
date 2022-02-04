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

class ManagementAgentModule extends FixedModule {

    ManagementAgentModule(Classpath cp) {
        super("jdk.management.agent", "17",
                Slices.of(
                        
                ),
                Slices.of(
                        cp.slice("jdk.internal.agent.*"),
                        cp.slice("jdk.internal.agent.resources.*"),
                        cp.slice("jdk.internal.agent.spi.*"),
                        cp.slice("sun.management.jdp.*"),
                        cp.slice("sun.management.jmxremote.*")
                ));
    }
}
