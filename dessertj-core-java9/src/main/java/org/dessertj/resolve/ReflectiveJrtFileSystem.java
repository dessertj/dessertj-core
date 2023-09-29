package org.dessertj.resolve;

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
