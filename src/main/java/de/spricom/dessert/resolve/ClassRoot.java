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

import de.spricom.dessert.matching.NamePattern;

import java.io.File;
import java.io.IOException;

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

    @Override
    public String toString() {
        return "root " + rootFile.getAbsolutePath();
    }
}
