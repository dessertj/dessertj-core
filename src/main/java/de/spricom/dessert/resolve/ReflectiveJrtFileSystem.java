package de.spricom.dessert.resolve;

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
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * To access the java runtime classes from Java 9 on, access to the JRT-FileSystem is need.
 * This requires NIO, which is not available for Java 6. Thus reflections is used for
 * this puropose. A better solution would be a multi-version jar.
 */
class ReflectiveJrtFileSystem {
    private final Class<?> fileSystems;
    private final Method newFileSystem;

    private final Class<?> fileSystem;
    private final Method getPath;

    private final Class<?> path;
    private final Method toUri;
    private final Method getFileName;

    private final Class<?> files;
    private final Method newDirectoryStream;
    private final Method isDirectory;

    private final Class<?> linkOption;
    private final Object emptyLinkOptionsArray;

    private final Object jrtFileSystem;

    ReflectiveJrtFileSystem() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        fileSystems = Class.forName("java.nio.file.FileSystems");
        newFileSystem = fileSystems.getMethod("newFileSystem", URI.class, Map.class);

        fileSystem = Class.forName("java.nio.file.FileSystem");
        getPath = fileSystem.getMethod("getPath", String.class, String[].class);

        path = Class.forName("java.nio.file.Path");
        toUri = path.getMethod("toUri");
        getFileName = path.getMethod("getFileName");

        files = Class.forName("java.nio.file.Files");
        newDirectoryStream = files.getMethod("newDirectoryStream", path);

        linkOption = Class.forName("java.nio.file.LinkOption");
        emptyLinkOptionsArray = Array.newInstance(linkOption, 0);
        isDirectory = files.getMethod("isDirectory", path, emptyLinkOptionsArray.getClass());

        jrtFileSystem = newFileSystem.invoke(null, URI.create("jrt:/"), Collections.emptyMap());
    }

    Object getModulePath(String moduleName) throws InvocationTargetException, IllegalAccessException {
        return getPath.invoke(jrtFileSystem, "/modules/" + moduleName, new String[0]);
    }

    boolean isDirectory(Object path) throws InvocationTargetException, IllegalAccessException {
        return (Boolean) isDirectory.invoke(null, path, emptyLinkOptionsArray);
    }

    Iterable<Object> newDirectoryStream(Object path) throws InvocationTargetException, IllegalAccessException {
        return (Iterable<Object>) newDirectoryStream.invoke(null, path);
    }

    URI toUri(Object path) throws InvocationTargetException, IllegalAccessException {
        return (URI) toUri.invoke(path);
    }

    String getFileName(Object path) throws InvocationTargetException, IllegalAccessException {
        return getFileName.invoke(path).toString();
    }

    List<String> listModules() throws InvocationTargetException, IllegalAccessException {
        List<String> moduleNames = new ArrayList<String>(64);
        Object modulesRoot = getPath.invoke(jrtFileSystem, "/modules", new String[0]);
        for (Object module : newDirectoryStream(modulesRoot)) {
            moduleNames.add(toUri(module).getPath().substring(1));
        }
        return moduleNames;
    }
}
