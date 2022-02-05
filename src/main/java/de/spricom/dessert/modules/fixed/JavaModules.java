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
import de.spricom.dessert.modules.core.ModuleLookup;
import de.spricom.dessert.modules.core.ModuleSlice;

/**
 * Generated by de.spricom.dessert.tools.GenerateStaticModulesTool.
 */
public final class JavaModules {

    public final ModuleSlice base;
    public final ModuleSlice compiler;
    public final ModuleSlice datatransfer;
    public final ModuleSlice desktop;
    public final ModuleSlice instrument;
    public final ModuleSlice logging;
    public final Management management;
    public final ModuleSlice naming;
    public final Net net;
    public final ModuleSlice prefs;
    public final ModuleSlice rmi;
    public final ModuleSlice scripting;
    public final ModuleSlice se;
    public final Security security;
    public final ModuleSlice smartcardio;
    public final Sql sql;
    public final Transaction transaction;
    public final Xml xml;

    public static final class Management extends DelegateModule {

        public final ModuleSlice rmi;

        Management(ModuleLookup registry) {
            super(registry.getModule("java.management"));
            rmi = registry.getModule("java.management.rmi");
        }
    }

    public static final class Net {

        public final ModuleSlice http;

        Net(ModuleLookup registry) {
            http = registry.getModule("java.net.http");
        }
    }

    public static final class Security {

        public final ModuleSlice jgss;
        public final ModuleSlice sasl;

        Security(ModuleLookup registry) {
            jgss = registry.getModule("java.security.jgss");
            sasl = registry.getModule("java.security.sasl");
        }
    }

    public static final class Sql extends DelegateModule {

        public final ModuleSlice rowset;

        Sql(ModuleLookup registry) {
            super(registry.getModule("java.sql"));
            rowset = registry.getModule("java.sql.rowset");
        }
    }

    public static final class Transaction {

        public final ModuleSlice xa;

        Transaction(ModuleLookup registry) {
            xa = registry.getModule("java.transaction.xa");
        }
    }

    public static final class Xml extends DelegateModule {

        public final ModuleSlice crypto;

        Xml(ModuleLookup registry) {
            super(registry.getModule("java.xml"));
            crypto = registry.getModule("java.xml.crypto");
        }
    }


    public JavaModules(ModuleLookup registry) {
        base = registry.getModule("java.base");
        compiler = registry.getModule("java.compiler");
        datatransfer = registry.getModule("java.datatransfer");
        desktop = registry.getModule("java.desktop");
        instrument = registry.getModule("java.instrument");
        logging = registry.getModule("java.logging");
        management = new Management(registry);
        naming = registry.getModule("java.naming");
        net = new Net(registry);
        prefs = registry.getModule("java.prefs");
        rmi = registry.getModule("java.rmi");
        scripting = registry.getModule("java.scripting");
        se = registry.getModule("java.se");
        security = new Security(registry);
        smartcardio = registry.getModule("java.smartcardio");
        sql = new Sql(registry);
        transaction = new Transaction(registry);
        xml = new Xml(registry);
    }
}
