package de.spricom.dessert.resolve;

/*-
 * #%L
 * Dessert Dependency Assertion Library
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

import de.spricom.dessert.classfile.ClassFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

final class JarClassEntry extends ClassEntry {
    private final JarFile jarFile;
    private final JarEntry jarEntry;

    JarClassEntry(ClassPackage pckg, JarFile jarFile, JarEntry jarEntry) {
        super(classname(jarEntry), pckg);
        this.jarFile = jarFile;
        this.jarEntry = jarEntry;
    }

    private static String classname(JarEntry jarEntry) {
        String cn = VersionsHelper.removeVersionPrefix(jarEntry.getName());
        return cn.substring(0, cn.length() - ".class".length()).replace('/', '.');
    }

    @Override
    public ClassFile resolveClassFile() {
        InputStream is = null;
        try {
            is = jarFile.getInputStream(jarEntry);
            ClassFile cf = new ClassFile(is);
            return cf;
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to read " + jarEntry.getName() + " from " + jarFile.getName(), ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    throw new IllegalStateException("Cannot close stream after reading " + jarEntry.getName() + " from " + jarFile.getName(), ex);
                }
            }
        }
    }

    @Override
    public URI getURI() {
        String uri = "jar:" + new File(jarFile.getName()).toURI().toASCIIString() + "!/" + jarEntry.getName();
        try {
            return new URI(uri);
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Cannot create URI from '" + uri + "'", ex);
        }
    }

}
