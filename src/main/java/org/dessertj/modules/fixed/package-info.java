/**
 * Generated classes to be used as constants for the JDK modules.
 *
 * Provides {@link org.dessertj.modules.fixed.JavaModules}
 * and {@link org.dessertj.modules.fixed.JdkModules} to be used like this:
 *
 * <pre>
 * public class ModulesDependenciesTest {
 *     private static final Classpath cp = new Classpath();
 *     private static final ModuleRegistry mr = new ModuleRegistry(cp);
 *     private static final JavaModules java = new JavaModules(mr);
 *     private static final JdkModules jdk = new JdkModules(mr);
 *
 *     private final Root dessertCode = cp.rootOf(Slice.class);
 *
 *     &#64;Test
 *     public void checkDessertDependencies() {
 *         dessert(dessertCode).usesOnly(java.base, java.logging);
 *         // show usage of JDK modules:
 *         dessert(dessertCode).usesNot(jdk.compiler, jdk.management.agent, java.management.rmi);
 *     }
 * }
 * </pre>
 */
package org.dessertj.modules.fixed;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2025 Hans Jörg Heßmann
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
