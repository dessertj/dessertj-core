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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectiveJrtFileSystemTest {

    private ReflectiveJrtFileSystem jrtFileSystem;

    @BeforeEach
    void init() throws IOException {
        jrtFileSystem = new ReflectiveJrtFileSystem();
    }

    @Test
    void testIsJrtFileSystemAvailable() {
        assertThat(ReflectiveJrtFileSystem.isJrtFileSystemAvailable()).isTrue();
    }

    @Test
    void testListModules() throws IOException {
        List<String> modules = jrtFileSystem.listModules();
        assertThat(modules).contains("java.base", "java.sql", "jdk.compiler");
    }

    @Test
    void testGetModulePath() {
        assertThat(someModulePath()).isInstanceOf(Path.class).hasToString("/modules/java.sql");
    }

    @Test
    void testIsDirectory() {
        assertThat(jrtFileSystem.isDirectory(someModulePath())).isTrue();
    }

    @Test
    void testToUri() throws URISyntaxException {
        URI uri = jrtFileSystem.toUri(someModulePath());
        assertThat(uri).hasNoHost().hasPath("/java.sql");
    }

    @Test
    void testGetFileName() {
        String filename = jrtFileSystem.getFileName(someModulePath());
        assertThat(filename).isEqualTo("java.sql");
    }

    @Test
    void testNewDirectoryStream() throws IOException {
        assertThat(someFilePath()).isInstanceOf(Path.class)
                .hasToString("/modules/java.sql/module-info.class");
    }

    private Object someModulePath() {
        return jrtFileSystem.getModulePath("java.sql");
    }

    private Object someFilePath() throws IOException {
        return StreamSupport.stream(jrtFileSystem.newDirectoryStream(someModulePath()).spliterator(), false)
                .filter(path -> !jrtFileSystem.isDirectory(path))
                        .findAny().orElseThrow(() -> new IllegalStateException("there is no file"));
    }
}
