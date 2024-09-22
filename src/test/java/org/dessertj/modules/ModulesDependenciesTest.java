package org.dessertj.modules;

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
import org.dessertj.modules.fixed.JavaModules;
import org.dessertj.modules.fixed.JdkModules;
import org.dessertj.slicing.Classpath;
import org.dessertj.slicing.Root;
import org.dessertj.slicing.Slice;
import org.junit.Test;

import static org.dessertj.assertions.SliceAssertions.dessert;

public class ModulesDependenciesTest {
    private static final Classpath cp = new Classpath();
    private static final ModuleRegistry mr = new ModuleRegistry(cp);
    private static final JavaModules java = new JavaModules(mr);
    private static final JdkModules jdk = new JdkModules(mr);

    private final Root dessertCode = cp.rootOf(Slice.class);

    @Test
    public void checkDessertDependencies() {
        dessert(dessertCode).usesOnly(java.base, java.logging);
        // show usage of JDK modules:
        dessert(dessertCode).usesNot(jdk.compiler, jdk.management.agent, java.management.rmi);
    }
}
