package de.spricom.dessert.modules.fixed;

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
import de.spricom.dessert.modules.core.DelegateModule;
import de.spricom.dessert.modules.core.Module;
import de.spricom.dessert.modules.core.ModuleLookup;

public final class JavaModules {

    public final Module base;
    public final Net net;
    public final Management management;

    static final class Net {
        public final Module http;

        public Net(ModuleLookup registry) {
            http = registry.getModule("java.net.http");
        }
    }

    static final class Management extends DelegateModule {

        public final Module rmi;

        public Management(ModuleLookup registry) {
            super(registry.getModule("java.management"));
            rmi = registry.getModule("java.management.rmi");
        }
    }

    public JavaModules(ModuleLookup registry) {
        base = registry.getModule("java.base");
        net = new Net(registry);
        management = new Management(registry);
    }
}
