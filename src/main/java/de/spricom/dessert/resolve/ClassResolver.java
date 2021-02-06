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
import de.spricom.dessert.util.Predicate;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * The class resolver provides fast access to the classes and packages to analyze.
 * Therefore it maintains of a list of all {@link ClassRoot} objects for which
 * each represents a classes directory or a JAR file. And it has to HashMaps
 * for all the packages and classes contained in any of these roots. The key
 * used for theses HashMaps is the full qualified class or package name.
 *
 * <p>The {@link ClassPackage} has a collection of all classes subpackages it contains.
 * Thus it provides direct access to all classes and subpackages of some package
 * within some root.</p>
 *
 * <p>Each {@link ClassPackage} or {@link ClassEntry} belongs to one root. The
 * same class or package name may appear within different roots. In that case the
 * ClassPackage or ClassEntry has {@link LinkedList} of all entries with the
 * same name. This lists can be accessed by {@link ClassPackage#getAlternatives()}
 * or {@link ClassEntry#getAlternatives()} respectively. Each entry points
 * to the same list of alternatives. If there are no alternatives the correspondig
 * list is null.</p>
 *
 * <p>Typically one of the static <i>of</i> methods should be used to create
 * a ClassResolver.</p>
 */
public final class ClassResolver implements TraversalRoot {
    private static Logger log = Logger.getLogger(ClassResolver.class.getName());

    private final List<ClassRoot> path = new ArrayList<ClassRoot>(60);
    private final ClassResolverCache cache = new ClassResolverCache();
    private boolean frozen;

    /**
     * Creates a ClassResolver for some arbitrary path.
     *
     * @param path the path to scan using the system specific classpath format
     * @return a ClassResolver with the corresponding entries
     * @throws IOException if a directory or jar file could not be read
     */
    public static ClassResolver of(String path) throws IOException {
        ClassResolver r = new ClassResolver();
        r.add(path);
        return r;
    }

    public static ClassResolver ofClassPath() throws IOException {
        ClassResolver r = new ClassResolver();
        r.addClassPath();
        return r;
    }

    public static ClassResolver ofClassPathWithoutJars() throws IOException {
        ClassResolver r = new ClassResolver();
        for (String entry : System.getProperty("java.class.path").split(File.pathSeparator)) {
            if (!entry.endsWith(".jar")) {
                r.addFile(entry);
            }
        }
        return r;
    }

    public static ClassResolver ofBootClassPath() throws IOException {
        ClassResolver r = new ClassResolver();
        r.addBootClassPath();
        return r;
    }

    public static ClassResolver ofClassPathAndBootClassPath() throws IOException {
        ClassResolver r = new ClassResolver();
        r.addClassPath();
        r.addBootClassPath();
        return r;
    }

    public void addClassPath() throws IOException {
        add(System.getProperty("java.class.path"));
    }

    public void addBootClassPath() throws IOException {
        String path = System.getProperty("sun.boot.class.path");
        // For JDK 9 there is no sun.boot.class.path property
        if (path != null) {
            add(path);
        }
    }

    public void add(String path) throws IOException {
        for (String entry : path.split(File.pathSeparator)) {
            addFile(entry);
        }
    }

    private void addFile(String filename) throws IOException {
        add(new File(filename));
    }

    public void add(File file) throws IOException {
        if (!file.exists()) {
            log.warning("Does not exist: " + file.getAbsolutePath());
        } else if (getRoot(file) != null) {
            log.warning("Already on path: " + file.getAbsolutePath());
        } else if (file.isDirectory()) {
            addRoot(new DirectoryRoot(file));
        } else if (file.isFile() && file.getName().endsWith(".jar")) {
            addRoot(new JarRoot(file));
        } else {
            log.warning("Don't know how to process: " + file.getAbsolutePath());
        }
    }

    public void addRoot(ClassRoot root) throws IOException {
        if (frozen) {
            throw new IllegalStateException("Cannot add root to a frozen ClassResolver.");
        }
        path.add(root);
        root.scan(cache);
    }

    public void freeze() {
        frozen = true;
    }

    public ClassRoot getRoot(File file) {
        for (ClassRoot root : path) {
            if (root.getRootFile().equals(file)) {
                return root;
            }
        }
        return null;
    }

    public ClassPackage getPackage(String packageName) {
        return cache.getPackage(packageName);
    }

    public ClassPackage getPackage(File root, String packageName) {
        ClassPackage pckg = getPackage(packageName);
        if (pckg == null) {
            return null;
        }
        if (root.equals(pckg.getRootFile())) {
            return pckg;
        }
        if (pckg.getAlternatives() != null) {
            for (ClassPackage alt : pckg.getAlternatives()) {
                if (root.equals(alt.getRootFile())) {
                    return alt;
                }
            }
        }
        return null;
    }

    public ClassEntry getClassEntry(String classname) {
        return cache.getClassEntry(classname);
    }

    public ClassEntry getClassEntry(File root, String classname) {
        ClassEntry ce = getClassEntry(classname);
        if (ce == null) {
            return null;
        }
        if (root.equals(ce.getPackage().getRootFile())) {
            return ce;
        }
        if (ce.getAlternatives() != null) {
            for (ClassEntry alt : ce.getAlternatives()) {
                if (root.equals(alt.getPackage().getRootFile())) {
                    return alt;
                }
            }
        }
        return null;
    }

    public void traverse(NamePattern pattern, ClassVisitor visitor) {
        for (ClassRoot classRoot : path) {
            classRoot.traverse(pattern, visitor);
        }
    }

    public Set<File> getRootFiles() {
        return getRootFiles(new Predicate<File>() {

            @Override
            public boolean test(File t) {
                return true;
            }
        });
    }

    public Set<File> getRootJars() {
        return getRootFiles(new Predicate<File>() {

            @Override
            public boolean test(File t) {
                return !t.isDirectory();
            }
        });
    }

    public Set<File> getRootDirs() {
        return getRootFiles(new Predicate<File>() {

            @Override
            public boolean test(File t) {
                return t.isDirectory();
            }
        });
    }

    public Set<File> getRootFiles(Predicate<File> predicate) {
        Set<File> files = new HashSet<File>();
        for (ClassRoot cr : path) {
            if (predicate.test(cr.getRootFile())) {
                files.add(cr.getRootFile());
            }
        }
        return files;
    }

    public Map<String, List<ClassEntry>> getDuplicates() {
        return cache.getDuplicates();
    }

    public int getPackageCount() {
        return cache.getPackageCount();
    }

    public int getClassCount() {
        return cache.getClassCount();
    }
}
