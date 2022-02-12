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

import de.spricom.dessert.matching.NamePattern;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.jar.Manifest;

public abstract class ClassRoot extends ClassPackage implements TraversalRoot {
    private final File rootFile;

    protected ClassRoot(File rootFile) {
        this.rootFile = rootFile;
    }

    protected abstract void scan(ClassCollector classCollector) throws IOException;

    public final void traverse(NamePattern pattern, ClassVisitor visitor) {
        traverse(pattern.matcher(), visitor);
    }

    @Override
    public final ClassRoot getRoot() {
        return this;
    }

    public final File getRootFile() {
        return rootFile;
    }

    public abstract URL getResource(String name);

    public InputStream getResourceAsStream(String name) {
        URL url = getResource(name);
        if (url == null) {
            return null;
        }
        try {
            return url.openStream();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Cannot read from " + url + ": " + ex, ex);
        }
    }

    public Manifest getManifest() throws IOException {
        InputStream inputStream = getResourceAsStream("META-INF/MANIFEST.MF");
        if (inputStream == null) {
            return null;
        }
        return new Manifest(inputStream);
    }

    public URI getURI() {
        return getRootFile().toURI();
    }

    @Override
    public String toString() {
        return "root " + rootFile.getAbsolutePath();
    }
}
