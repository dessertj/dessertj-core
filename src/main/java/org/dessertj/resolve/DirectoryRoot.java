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

import org.dessertj.matching.NamePattern;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

final class DirectoryRoot extends ClassRoot {

    private Map<Integer, ClassPackage> versions;

    public DirectoryRoot(File dir) {
        super(dir);
    }

    @Override
    protected void scan(ClassCollector collector) {
        scan(collector, this, getRootFile(), "", null);
    }

    private void scan(ClassCollector collector, ClassPackage pckg, File dir, String prefix, Integer version) {
        collector.addPackage(pckg);
        for (File file : dir.listFiles()) {
            String filename = file.getName();
            if (file.isDirectory()) {
                if (prefix.equalsIgnoreCase("meta-inf.versions.")) {
                    Integer ver = Integer.parseInt(filename);
                    VersionRoot versionRoot = new VersionRoot(this, ver);
                    if (versions == null) {
                        versions = new HashMap<Integer, ClassPackage>();
                    }
                    versions.put(ver, versionRoot);
                    scan(collector, versionRoot, file, "", ver);
                } else {
                    String packageName = prefix + filename;
                    ClassPackage subPackage = new ClassPackage(pckg, packageName);
                    scan(collector, subPackage, file, packageName + ".", version);
                }
            } else if (filename.endsWith(".class")) {
                ClassEntry classEntry = new DirectoryClassEntry(pckg, file, version);
                pckg.addClass(classEntry);
                collector.addClass(classEntry);
            }
        }
    }

    @Override
    public void traverse(NamePattern pattern, ClassVisitor visitor) {
        traverse(pattern.matcher(), visitor);
        if (versions != null) {
            for (ClassPackage root : versions.values()) {
                root.traverse(pattern.matcher(), visitor);
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
