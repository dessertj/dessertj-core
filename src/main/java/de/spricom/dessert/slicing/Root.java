package de.spricom.dessert.slicing;

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

import de.spricom.dessert.resolve.ClassRoot;

import java.net.URI;

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
        return root.getRootFile().toURI();
    }

    @Override
    Classpath getClasspath() {
        return classpath;
    }

    @Override
    boolean isConcrete() {
        return true;
    }

    public String toString() {
        return "root of " + root.getRootFile().getName();
    }
}
