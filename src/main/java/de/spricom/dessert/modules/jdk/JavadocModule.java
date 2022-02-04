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

class JavadocModule extends FixedModule {

    JavadocModule(Classpath cp) {
        super("jdk.javadoc", "17",
                Slices.of(
                        cp.slice("jdk.javadoc.doclet.*")
                ),
                Slices.of(
                        cp.slice("jdk.javadoc.doclet.*"),
                        cp.slice("jdk.javadoc.internal.*"),
                        cp.slice("jdk.javadoc.internal.api.*"),
                        cp.slice("jdk.javadoc.internal.doclets.formats.html.*"),
                        cp.slice("jdk.javadoc.internal.doclets.formats.html.markup.*"),
                        cp.slice("jdk.javadoc.internal.doclets.formats.html.resources.*"),
                        cp.slice("jdk.javadoc.internal.doclets.toolkit.*"),
                        cp.slice("jdk.javadoc.internal.doclets.toolkit.builders.*"),
                        cp.slice("jdk.javadoc.internal.doclets.toolkit.resources.*"),
                        cp.slice("jdk.javadoc.internal.doclets.toolkit.resources.releases.*"),
                        cp.slice("jdk.javadoc.internal.doclets.toolkit.taglets.*"),
                        cp.slice("jdk.javadoc.internal.doclets.toolkit.util.*"),
                        cp.slice("jdk.javadoc.internal.doclets.toolkit.util.links.*"),
                        cp.slice("jdk.javadoc.internal.doclint.*"),
                        cp.slice("jdk.javadoc.internal.doclint.resources.*"),
                        cp.slice("jdk.javadoc.internal.tool.*"),
                        cp.slice("jdk.javadoc.internal.tool.resources.*")
                ));
    }
}
