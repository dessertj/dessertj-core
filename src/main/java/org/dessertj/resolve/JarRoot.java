package org.dessertj.resolve;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2025 Hans Jörg Heßmann
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

import org.dessertj.util.Assertions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

final class JarRoot extends ClassRoot {
    private JarFile jarFileArchive;

    public JarRoot(File jarFile) {
        super(jarFile);
    }

    @Override
    protected void scan(ClassCollector collector) throws IOException {
        Map<String, ClassPackage> packages = new HashMap<String, ClassPackage>();
        packages.put("", this);
        collector.addPackage(this);
        HashMap<Integer, Map<String, ClassPackage>> versions = new HashMap<Integer, Map<String, ClassPackage>>();

        JarFile jarFile = getJarFileArchive();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (!entry.isDirectory()
                    && entry.getName().endsWith(".class")) {
                addClass(collector, packages, jarFile, entry, versions);
            }
        }
        // JarFile must not be closed to be able to access the content of each JarEntry.
    }

    private JarFile getJarFileArchive() throws IOException {
        if (jarFileArchive == null) {
            jarFileArchive = new JarFile(getRootFile());
        }
        return jarFileArchive;
    }

    private void addClass(ClassCollector collector, Map<String, ClassPackage> packages,
                          JarFile jarFile, JarEntry entry,
                          Map<Integer, Map<String, ClassPackage>> versions) {
        VersionsHelper.VersionInfo versionInfo = VersionsHelper.matchVersion(entry.getName());
        if (versionInfo == null) {
            ClassPackage pckg = ensurePackage(collector, packages, packageName(entry.getName()));
            ClassEntry ce = new JarClassEntry(classname(entry.getName()), pckg, null, jarFile, entry);
            pckg.addClass(ce);
            collector.addClass(ce);
        } else {
            Map<String, ClassPackage> versionPackages = versions.get(versionInfo.version);
            if (versionPackages == null) {
                versionPackages = new HashMap<String, ClassPackage>();
                VersionRoot versionRoot = new VersionRoot(this, versionInfo.version);
                addVersion(versionRoot);
                versionPackages.put("", versionRoot);
                collector.addPackage(versionRoot);
                versions.put(versionInfo.version, versionPackages);
            }
            ClassPackage pckg = ensurePackage(collector, versionPackages, packageName(versionInfo.path));
            ClassEntry ce = new JarClassEntry(classname(versionInfo.path), pckg, versionInfo.version, jarFile, entry);
            pckg.addClass(ce);
            collector.addClass(ce);
        }
    }

    private static String classname(String path) {
        return path.substring(0, path.length() - ".class".length()).replace('/', '.');
    }

    private String packageName(String path) {
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

    @Override
    public URL getResource(String name) {
        JarEntry jarEntry = getJarEntry(name);
        if (jarEntry == null) {
            return null;
        }
        try {
            return new URL("jar:" + getRootFile().toURI().toURL() + "!/" + jarEntry.getName());
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Unable to convert " + getRootFile().toURI() + " to an URL: " + ex, ex);
        }
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        JarEntry jarEntry = getJarEntry(name);
        if (jarEntry == null) {
            return null;
        }
        try {
            return jarFileArchive.getInputStream(jarEntry);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to read " + jarEntry.getName()
                    + " from jar-archive " + getRootFile().getPath() + ": " + ex, ex);
        }
    }

    public JarEntry getJarEntry(String name) {
        Assertions.notNull(name, "name");
        try {
            if (name.startsWith("/")) {
                name = name.substring(1);
            }
            JarEntry jarEntry = getJarFileArchive().getJarEntry(name);
            return jarEntry;
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to read jar-archive " + getRootFile().getPath() + ": " + ex, ex);
        }
    }

    public Manifest getManifest() throws IOException {
        JarFile jarFile = getJarFileArchive();
        Manifest manifest = jarFile.getManifest();
        return manifest;
    }
}
