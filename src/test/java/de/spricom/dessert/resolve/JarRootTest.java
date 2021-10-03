package de.spricom.dessert.resolve;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2021 Hans Jörg Heßmann
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

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.Manifest;

import static org.fest.assertions.Assertions.assertThat;

public class JarRootTest {
    private JarRoot jarRoot;

    @Before
    public void init() throws IOException {
        ClassResolver resolver = ClassResolver.ofClassPath();
        for (File jar : resolver.getRootJars()) {
            if (jar.getName().startsWith("junit-4.")) {
                jarRoot = new JarRoot(jar);
                break;
            }
        }
        assertThat(jarRoot).as("No junit-4.* found on java.class.path").isNotNull();
        System.out.println("Using " + jarRoot.getRootFile().getAbsolutePath());
    }

    @Test
    public void testGetTopLevelResource() {
        URL expectedUrl = Test.class.getResource("/LICENSE-junit.txt");
        URL actualUrl = jarRoot.getResource("LICENSE-junit.txt");
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    public void testGetNestedResource() {
        URL expectedUrl = Test.class.getResource("/junit/runner/logo.gif");
        URL actualUrl = jarRoot.getResource("junit/runner/logo.gif");
        URL absoluteUrl = jarRoot.getResource("/junit/runner/logo.gif");
        assertThat(actualUrl).isEqualTo(expectedUrl);
        assertThat(absoluteUrl).isEqualTo(expectedUrl);
    }

    @Test
    public void testReadResource() throws IOException {
        String text = new String(IOUtils.readAll(jarRoot.getResourceAsStream("/LICENSE-junit.txt")));
        assertThat(text).contains("Eclipse Public License");
    }

    @Test
    public void testNonExistingResource() {
        assertThat(jarRoot.getResource("does/not/exist.xy")).isNull();
        assertThat(jarRoot.getResourceAsStream("does/not/exist.xy")).isNull();
    }

    @Test
    public void testManifest() throws IOException {
        Manifest expectedManifest = new Manifest(jarRoot.getResourceAsStream("META-INF/MANIFEST.MF"));
        Manifest actualManifest = jarRoot.getManifest();
        String moduleName = actualManifest.getMainAttributes().getValue("Automatic-Module-Name");
        assertThat(actualManifest).isEqualTo(expectedManifest);
        assertThat(moduleName).isEqualTo("junit");
    }

}
