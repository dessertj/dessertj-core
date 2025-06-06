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

import org.dessertj.matching.NamePattern;
import org.dessertj.util.Predicate;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class resolver provides fast access to the classes and packages to analyze.
 * Therefore, it maintains of a list of all {@link ClassRoot} objects for which
 * each represents a classes directory or a JAR file. And it has two HashMaps
 * for all the packages and classes contained in any of these roots. The key
 * used for theses HashMaps is the fully qualified class or package name.
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
 * to the same list of alternatives. If there are no alternatives the corresponding
 * list is null.</p>
 *
 * <p>Typically one of the static <i>of</i> methods should be used to create
 * a ClassResolver.</p>
 */
public final class ClassResolver implements TraversalRoot {
    private static final Logger log = Logger.getLogger(ClassResolver.class.getName());

    private final List<ClassRoot> path = new ArrayList<ClassRoot>(60);
    private final ClassResolverCache cache = new ClassResolverCache();
    private boolean ignoreManifest;
    private boolean frozen;

    /**
     * Creates a ClassResolver for some arbitrary path.
     *
     * @param path the path to scan using the system specific classpath format
     * @return a ClassResolver with the corresponding entries
     */
    public static ClassResolver of(String path) {
        ClassResolver r = new ClassResolver();
        r.add(path);
        return r;
    }

    /**
     * Creates a ClassResolver based on the <i>java.class.path</i> system-property.
     * For each JAR that contains a Manifest file with a <i>Class-Path</i> attribute,
     * those entries will be added, too, recursively.
     *
     * @return a ClassResolver with the corresponding entries
     * @throws ResolveException if a directory or jar file could not be read
     */
    public static ClassResolver ofClassPath() {
        ClassResolver r = new ClassResolver();
        r.addClassPath();
        return r;
    }

    /**
     * Creates a ClassResolver with all entries for {@link #ofClassPath()} and all entries
     * from the <i>sun.boot.class.path</i> system-properties for java 8 and before or
     * all java runtime modules from Java 9 on.
     *
     * @return a ClassResolver with the corresponding entries
     * @throws ResolveException if a directory or jar file could not be read
     */
    public static ClassResolver ofClassPathAndJavaRuntime() {
        long ts = System.nanoTime();
        ClassResolver r = new ClassResolver();
        r.addClassPath();
        r.addBootClassPath();
        r.addJavaRuntimeModules();
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("Needed %.1f ms to scan classes form java.class.path and runtime.",
                    (System.nanoTime() - ts) / 1e6));
        }
        return r;
    }

    /**
     * Creates a ClassResolver containing only the directories on the <i>java.class.path</i> system-property.
     *
     * @return a ClassResolver with the corresponding entries
     * @throws ResolveException if a directory or jar file could not be read
     */
    public static ClassResolver ofClassPathWithoutJars() {
        ClassResolver r = new ClassResolver();
        for (String entry : System.getProperty("java.class.path").split(File.pathSeparator)) {
            if (!entry.endsWith(".jar")) {
                r.addFile(entry);
            }
        }
        return r;
    }

    /**
     * Creates a ClassResolver similar to {@link #ofClassPath()}, but without the entries
     * from the <i>Class-Path</i> attribute of any Manifest file. The {@link #isIgnoreManifest()}
     * flag of the resulting ClassResolver will be true.
     *
     * @return a ClassResolver with the corresponding entries
     * @throws ResolveException if a directory or jar file could not be read
     */
    public static ClassResolver ofClassPathIgnoringManifests() {
        ClassResolver r = new ClassResolver();
        r.setIgnoreManifest(true);
        r.addClassPath();
        return r;
    }

    public void addClassPath() {
        add(System.getProperty("java.class.path"));
    }

    public void addBootClassPath() {
        String path = System.getProperty("sun.boot.class.path");
        // For JDK 9 there is no sun.boot.class.path property
        if (path != null) {
            add(path);
        }
    }

    /**
     * Adds all modules form the Java Runtime System. For Java 8 and older this
     * method has no effect
     *
     * @throws ResolveException modules could not be read
     */
    public void addJavaRuntimeModules() {
        if (!isJrtFileSystemAvailable()) {
            return;
        }
        try {
            ReflectiveJrtFileSystem fs = new ReflectiveJrtFileSystem();
            for (String module : fs.listModules()) {
                addRoot(new JrtModuleRoot(module, fs));
            }
        } catch (IOException ex) {
            throw new ResolveException("Unable to read java runtime modules: " + ex.getMessage(), ex);
        } catch (InvocationTargetException ex) {
            if (ex.getTargetException() instanceof IOException) {
                throw new ResolveException("Unable to read java runtime modules: " +
                        ex.getTargetException().getMessage(), ex.getTargetException());
            }
            throw new ResolveException("Unable to read java runtime modules.", ex);
        } catch (ClassNotFoundException ex) {
            throw new ResolveException("Cannot access NIO classes.", ex);
        } catch (NoSuchMethodException ex) {
            throw new ResolveException("Cannot access NIO classes.", ex);
        } catch (IllegalAccessException ex) {
            throw new ResolveException("Cannot access NIO classes.", ex);
        } catch (URISyntaxException ex) {
            throw new ResolveException("Cannot convert jrt-URI.", ex);
        }
    }

    public static boolean isJrtFileSystemAvailable() {
        return ReflectiveJrtFileSystem.isJrtFileSystemAvailable();
    }

    public void add(String path) {
        for (String entry : path.split(File.pathSeparator)) {
            addFile(entry);
        }
    }

    private void addFile(String filename) {
        add(new File(filename));
    }

    public void add(File file) {
        try {
            if (!file.exists()) {
                log.warning("Does not exist: " + file.getAbsolutePath());
            } else if (getRoot(file) != null) {
                log.warning("Already on path: " + file.getAbsolutePath());
            } else if (file.isDirectory()) {
                addRoot(new DirectoryRoot(file));
            } else if (file.isFile() && file.getName().endsWith(".jar")) {
                JarRoot root = new JarRoot(file);
                addRoot(root);
                if (!ignoreManifest) {
                    addManifestClassPath(root);
                }
            } else {
                log.warning("Don't know how to process: " + file.getAbsolutePath());
            }
        } catch (IOException ex) {
            throw new ResolveException("Unable to resolve " + file.getAbsolutePath() + ": " + ex.getMessage(), ex);
        }
    }

    public void addRoot(ClassRoot root) throws IOException {
        if (frozen) {
            throw new IllegalStateException("Cannot add root to a frozen ClassResolver.");
        }
        path.add(root);
        long ts = System.nanoTime();
        root.scan(cache);
        if (log.isLoggable(Level.FINER)) {
            log.fine(String.format("Needed %.1f ms to scan classes form %s.",
                    (System.nanoTime() - ts) / 1e6,
                    root.getURI()));
        }
    }

    /**
     * Adds all entries from the <i>Class-Path</i> attribute of the JAR's Manifest file.
     * Does nothing if there is no Manifest file or no <i>Class-Path</i> attribute.
     * Processes JAR files recursively unless {@link #isIgnoreManifest()} has been set.
     * Use {@link #getRoot(File)} to get the {@link JarRoot} for a file.
     *
     * @param jarRoot the jar to process
     * @throws IOException if the Manifest could not be read
     */
    public void addManifestClassPath(JarRoot jarRoot) throws IOException {
        Manifest manifest = jarRoot.getManifest();
        if (manifest == null) {
            return;
        }
        String classpath = manifest.getMainAttributes().getValue("Class-Path");
        if (classpath == null) {
            return;
        }
        URL context = jarRoot.getRootFile().toURI().toURL();
        for (String relativeUrl : classpath.split("\\s+")) {
            try {
                File file = new File(new URL(context, relativeUrl).toURI().getPath());
                if (file.exists()) {
                    add(file);
                } else {
                    log.info("Does not exist: " + file.getAbsolutePath() +
                            " (referenced by Manifest of " + context + ")");
                }
            } catch (URISyntaxException ex) {
                log.warning("Unable to parse relative path " + relativeUrl
                        + " within Manifest of " + context + ": " + ex.getMessage());
            }
        }
    }

    public boolean isIgnoreManifest() {
        return ignoreManifest;
    }

    /**
     * If set the <i>Class-Path</i> attribute of JAR's Manifest files will be ignored.
     *
     * @param ignoreManifest the flag
     */
    public void setIgnoreManifest(boolean ignoreManifest) {
        this.ignoreManifest = ignoreManifest;
    }

    /**
     * After freezing any modification to the path represented by this resolver will
     * result in an {@link IllegalStateException}.
     */
    public void freeze() {
        frozen = true;
    }

    public ClassRoot getRoot(File file) {
        for (ClassRoot root : path) {
            if (root.getRootFile() != null && root.getRootFile().equals(file)) {
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
        URI rootUri = root.toURI();
        if (rootUri.equals(pckg.getRoot().getURI())) {
            return pckg;
        }
        if (pckg.getAlternatives() != null) {
            for (ClassPackage alt : pckg.getAlternatives()) {
                if (rootUri.equals(alt.getRoot().getURI())) {
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
        URI rootUri = root.toURI();
        if (rootUri.equals(ce.getPackage().getRoot().getURI())) {
            return ce;
        }
        if (ce.getAlternatives() != null) {
            for (ClassEntry alt : ce.getAlternatives()) {
                if (rootUri.equals(alt.getPackage().getRoot().getURI())) {
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

    public List<ClassRoot> getPath() {
        return path;
    }

    public Set<File> getRootFiles() {
        return getRootFiles(new Predicate<File>() {

            @Override
            public boolean test(File t) {
                return t != null;
            }
        });
    }

    public Set<File> getRootJars() {
        return getRootFiles(new Predicate<File>() {

            @Override
            public boolean test(File t) {
                return t != null && !t.isDirectory();
            }
        });
    }

    public Set<File> getRootDirs() {
        return getRootFiles(new Predicate<File>() {

            @Override
            public boolean test(File t) {
                return t != null && t.isDirectory();
            }
        });
    }

    public Set<File> getRootFiles(Predicate<File> predicate) {
        Set<File> files = new HashSet<File>(path.size());
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (File root : getRootFiles()) {
            if (sb.length() == 0) {
                sb.append("classpath ");
            } else {
                sb.append(File.pathSeparator);
            }
            sb.append(root.getAbsolutePath());
        }
        return sb.toString();
    }
}
