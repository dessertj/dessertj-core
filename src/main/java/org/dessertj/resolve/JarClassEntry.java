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

import org.dessertj.classfile.ClassFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

final class JarClassEntry extends ClassEntry {
    private static final Logger log = Logger.getLogger(JarClassEntry.class.getName());

    private final JarFile jarFile;
    private final JarEntry jarEntry;

    JarClassEntry(String classname, ClassPackage pckg, Integer version, JarFile jarFile, JarEntry jarEntry) {
        super(classname, pckg, version);
        this.jarFile = jarFile;
        this.jarEntry = jarEntry;
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
                    log.log(Level.SEVERE, "Cannot close stream after reading " + jarEntry.getName() + " from " + jarFile.getName(), ex);
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
