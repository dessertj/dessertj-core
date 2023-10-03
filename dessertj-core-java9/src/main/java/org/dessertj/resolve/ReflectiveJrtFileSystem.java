package org.dessertj.resolve;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2023 Hans Jörg Heßmann
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Alternative Implementation for Java 9 and above that does not use reflection.
 */
class ReflectiveJrtFileSystem {

    private final FileSystem jrtFileSystem;

    ReflectiveJrtFileSystem() throws IOException {
        jrtFileSystem = FileSystems.newFileSystem(URI.create("jrt:/"), Collections.emptyMap());
    }

    static boolean isJrtFileSystemAvailable() {
        return true;
    }

    Object getModulePath(String moduleName) {
        return jrtFileSystem.getPath("/modules/" + moduleName);
    }

    boolean isDirectory(Object path) {
        return Files.isDirectory((Path) path);
    }

    Iterable<?> newDirectoryStream(Object path) throws IOException {
        return Files.newDirectoryStream((Path) path);
    }

    URI toUri(Object path) throws URISyntaxException {
        URI uri = ((Path) path).toUri();
        if (uri.toASCIIString().startsWith("jrt:/modules/")) {
            // Work-around for Java 11
            uri = new URI(uri.toASCIIString().replace("jrt:/modules/", "jrt:/"));
        }
        return uri;
    }

    String getFileName(Object path) {
        return ((Path) path).getFileName().toString();
    }

    List<String> listModules() throws IOException {
        List<String> moduleNames = new ArrayList<String>(64);
        Path modulesRoot = jrtFileSystem.getPath("/modules");
        for (Path dir : Files.newDirectoryStream(modulesRoot)) {
            String path = dir.toUri().getPath();
            moduleNames.add(path.substring(path.lastIndexOf('/') + 1));
        }
        return moduleNames;
    }
}
