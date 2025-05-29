package org.dessertj.resolve;

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

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;

public class DirectoryRootTest {
    private DirectoryRoot directoryRoot;

    @Before
    public void init() {
        ClassResolver resolver = ClassResolver.ofClassPath();
        ClassEntry myEntry = resolver.getClassEntry(this.getClass().getName());
        ClassRoot root = myEntry.getPackage().getRoot();
        assertThat(root).isInstanceOf(DirectoryRoot.class);
        directoryRoot = (DirectoryRoot) root;
        System.out.println("Using " + directoryRoot.getRootFile().getAbsolutePath());
    }

    @Test
    public void testGetResource() {
        URL expectedUrl = this.getClass().getResource(this.getClass().getSimpleName() + ".class");
        assertThat(expectedUrl).isNotNull();
        assertThat(expectedUrl.getProtocol()).isEqualTo("file");
        String path = getPath();

        URL actualUrl = directoryRoot.getResource(path);
        URL absoluteUrl = directoryRoot.getResource("/" + path);

        assertThat(actualUrl).isEqualTo(expectedUrl);
        assertThat(absoluteUrl).isEqualTo(expectedUrl);
    }

    private String getPath() {
        return this.getClass().getName().replace('.', '/') + ".class";
    }

    @Test
    public void testGetResourceAsStream() throws IOException {
        byte[] expectedBytes = IOUtils.readAll(
                this.getClass().getResourceAsStream(this.getClass().getSimpleName() + ".class"));
        String path = getPath();

        byte[] actualBytes = IOUtils.readAll(directoryRoot.getResourceAsStream(path));

        assertThat(actualBytes).isEqualTo(expectedBytes);
    }

    @Test
    public void testNonExistingResource() {
        assertThat(directoryRoot.getResource("does/not/exist.xy")).isNull();
        assertThat(directoryRoot.getResourceAsStream("does/not/exist.xy")).isNull();
    }

    @Test
    public void testNonExistingManifest() throws IOException {
        assertThat(directoryRoot.getManifest()).isNull();
    }

}
