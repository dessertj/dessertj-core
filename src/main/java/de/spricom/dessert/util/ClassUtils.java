package de.spricom.dessert.util;

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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Collection of utility methods to work with classes.
 */
public final class ClassUtils {

    private ClassUtils(){}

    /**
     * Each .class file has a unique URI combined from the URI to its root, the path within
     * the package structure and the filename.
     *
     * @param clazz the {@link Class} to determine the {@link URI} for
     * @return the URI
     */
    public static URI getURI(Class<?> clazz) {
        URL url = clazz.getResource(getShortName(clazz) + ".class");
        assert url != null : "Cannot find resource for " + clazz;
        try {
            return url.toURI();
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Cannot convert '" + url + "' to URI", ex);
        }
    }

    /**
     * Gets the classname without the package prefix.
     * For inner classes this is different from {@link Class#getSimpleName()}.
     *
     * @param clazz the class the determine the name for
     * @return the classname without package prefix
     */
    public static String getShortName(Class<?> clazz) {
        String name = clazz.getName();
        int index = name.lastIndexOf('.');
        return name.substring(index + 1);
    }

    public static final File getRootFile(Class<?> clazz) {
        String filename = "/" + clazz.getName().replace('.', '/') + ".class";
        URL url = clazz.getResource(filename);
        assert url != null : "Resource " + filename + " not found!";
        if ("file".equals(url.getProtocol())) {
            assert url.getFile().endsWith(filename) : url + " does not end with " + filename;
            return new File(url.getFile().substring(0, url.getFile().length() - filename.length()));
        } else if ("jar".equals(url.getProtocol())) {
            assert url.getFile().startsWith("file:") : url + " does not start with jar:file";
            assert url.getFile().endsWith(".jar!" + filename) : url + " does not end with .jar!" + filename;
            try {
                return new File(URLDecoder.decode(url.getFile().substring("file:".length(), url.getFile().length() - filename.length() - 1), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                throw new IllegalStateException("UTF-8 encoding not supported!", ex);
            }
        } else {
            throw new IllegalArgumentException("Unknown protocol in " + url);
        }
    }

    public static boolean isPublic(Class<?> clazz) {
        return Modifier.isPublic(clazz.getModifiers());
    }

    public static boolean isInnerType(Class<?> clazz) {
        return clazz.getEnclosingClass() != null;
    }
}
