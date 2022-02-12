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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

final class DirectoryRoot extends ClassRoot {
    public DirectoryRoot(File dir) {
        super(dir);
    }

    @Override
    protected void scan(ClassCollector collector) throws IOException {
        scan(collector, this, getRootFile(), "");
    }

    private void scan(ClassCollector collector, ClassPackage pckg, File dir, String prefix) {
        collector.addPackage(pckg);
        for (File file : dir.listFiles()) {
            String filename = file.getName();
            if (file.isDirectory()) {
                String packageName = prefix + filename;
                ClassPackage subPackage = new ClassPackage(pckg, packageName);
                scan(collector, subPackage, file, packageName + ".");
            } else if (filename.endsWith(".class")) {
                ClassEntry classEntry = new DirectoryClassEntry(pckg, file);
                pckg.addClass(classEntry);
                collector.addClass(classEntry);
            }
        }
    }

    @Override
    public URL getResource(String name) {
        File file = new File(getRootFile(), name);
        if (!file.exists()) {
            return null;
        }
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Cannot get URL for " + file.getPath() + ": " + ex, ex);
        }
    }
}
