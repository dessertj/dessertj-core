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

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

final class JarRoot extends ClassRoot {
    public JarRoot(File jarFile) throws IOException {
        super(jarFile);
    }

    @Override
    protected void scan(ClassCollector collector) throws IOException {
        Map<String, ClassPackage> packages = new HashMap<String, ClassPackage>();
        packages.put("", this);
        collector.addPackage(this);

        JarFile jarFile = new JarFile(getRootFile());
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (!entry.isDirectory()
                    && entry.getName().endsWith(".class")
                    && !entry.getName().startsWith("META-INF/")) {
                // TODO: Proper handling of multi-release jar files.
                addClass(collector, packages, jarFile, entry);
            }
        }
        // JarFile must not be closed to be able to access the content of each JarEntry.
    }

    private void addClass(ClassCollector collector, Map<String, ClassPackage> packages, JarFile jarFile, JarEntry entry) throws IOException {
        ClassPackage pckg = ensurePackage(collector, packages, packageName(entry));
        ClassEntry ce = new JarClassEntry(pckg, jarFile, entry);
        pckg.addClass(ce);
        collector.addClass(ce);
    }

    private String packageName(JarEntry entry) {
        String path = VersionsHelper.removeVersionPrefix(entry.getName());
        return packageName(path, '/').replace('/', '.');
    }

    private ClassPackage ensurePackage(ClassCollector collector, Map<String, ClassPackage> packages, String packageName) {
        ClassPackage pckg = packages.get(packageName);
        if (pckg != null) {
            return pckg;
        }
        ClassPackage parent = ensurePackage(collector, packages, parentPackageName(packageName));
        pckg = new ClassPackage(parent, packageName);
        collector.addPackage(pckg);
        packages.put(packageName, pckg);
        return pckg;
    }

    private String parentPackageName(String packageName) {
        return packageName(packageName, '.');
    }

    private String packageName(String name, char separator) {
        int index = name.lastIndexOf(separator);
        if (index == -1) {
            return "";
        }
        String packageName = name.substring(0, index);
        return packageName;
    }
}
