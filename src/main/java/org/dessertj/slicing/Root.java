package org.dessertj.slicing;

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

import org.dessertj.resolve.ClassRoot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.jar.Manifest;

/**
 * A special {@link Slice} that represents a whole JAR file, classes directory, module or other
 * single source of classes. The slice contains all its .class files.
 */
public class Root extends AbstractRootSlice {
    private final ClassRoot root;
    private final Classpath classpath;

    Root(ClassRoot root, Classpath classpath) {
        super(root);
        this.root = root;
        this.classpath = classpath;
    }

    public URI getURI() {
        return root.getURI();
    }

    @Override
    Classpath getClasspath() {
        return classpath;
    }

    @Override
    boolean isConcrete() {
        return true;
    }

    public File getRootFile() {
        return root.getRootFile();
    }

    public URL getResource(String name) {
        return root.getResource(name);
    }

    public InputStream getResourceAsStream(String name) {
        return root.getResourceAsStream(name);
    }

    public Manifest getManifest() throws IOException {
        return root.getManifest();
    }

    public String toString() {
        return root.toString();
    }
}
