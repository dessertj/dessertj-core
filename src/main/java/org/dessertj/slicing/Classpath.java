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

import org.dessertj.resolve.ClassEntry;
import org.dessertj.resolve.ClassResolver;
import org.dessertj.resolve.ClassRoot;
import org.dessertj.resolve.ResolveException;
import org.dessertj.util.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Classpath is the starting-point for any dessertj unit-test.
 * All slices used within a test are created from the Classpath.
 * <p><b>Important:</b>All slices used for assertions must stem from the same Classpath.
 * Otherwise the behaviour is undefined.</p>
 */
public final class Classpath extends AbstractRootSlice {
    private static final Logger log = Logger.getLogger(Classpath.class.getName());
    private static ClassResolver defaultResolver;

    private final ClassResolver resolver;

    private final Map<String, Clazz> classes = new HashMap<String, Clazz>();

    /**
     * Creates a Classpath instance by using the default resolver. Thus, the resulting
     * Classpath contains all directories and .jar files found on the path given
     * by the <i>java.class.path</i> system property.
     */
    public Classpath() {
        this(getDefaultResolver());
    }

    /**
     * Creates a Classpath with some custom resolver. With this method any custom
     * ClassPath can be created.
     *
     * @param resolver the resolver to resolve the classes
     */
    public Classpath(ClassResolver resolver) {
        super(resolver);
        this.resolver = resolver;
        resolver.freeze();
    }

    private static ClassResolver getDefaultResolver() {
        if (defaultResolver == null) {
            defaultResolver = ClassResolver.ofClassPathAndJavaRuntime();
        }
        return defaultResolver;
    }

    Clazz asClazz(ClassEntry ce) {
        Clazz clazz = classes.get(ce.getClassname());
        if (clazz == null) {
            clazz = new Clazz(this, ce);
            classes.put(ce.getClassname(), clazz);
            return clazz;
        } else {
            Clazz alt = clazz.getAlternative(ce);
            assert alt != null : "alternative for " + ce.getURI() + " is null";
            return alt;
        }
    }

    /**
     * Creates a {@link Clazz} form a classname.
     * Returns the first matching class on this classpath, if there is one. If no such class
     * could be found it tries to use the current {@link ClassLoader} to look up the class.
     * Of both fail a place-holder object will be returned that contains nothing but the classname.
     *
     * @param classname the classname
     * @return the Clazz
     */
    public Clazz asClazz(String classname) {
        Clazz clazz = classes.get(classname);
        if (clazz == null) {
            clazz = resolveClazz(classname);
            if (clazz == null) {
                clazz = loadClass(classname);
            }
            if (clazz == null) {
                clazz = undefined(classname);
            }
            classes.put(classname, clazz);
        }
        return clazz;
    }

    /**
     * Creates a {@link Clazz} form a Java {@link Class}.
     * Returns a Clazz matching exactly to the passed {@link Class}. If there are alternatives on this
     * Classpath they will be linked to the Clazz returned.
     *
     * <p><b>Beware:</b>Creating a Clazz for a Class not found on the current Classpath for which an other
     * class of the same name is on the Classpath my alter an existing Clazz by adding an alternative.
     * This rare case may change the result of {@link #duplicates()} or specific slice assertions.</p>
     *
     * @param classImpl the Java class
     * @return the Clazz
     */
    public Clazz asClazz(Class<?> classImpl) {
        URI uri = ClassUtils.getURI(classImpl);
        String classname = classImpl.getName();
        Clazz clazz = classes.get(classname);
        if (clazz == null) {
            clazz = resolveClazz(classname);
            if (clazz != null) {
                classes.put(classname, clazz);
            }
        }
        if (clazz != null) {
            for (Clazz alternative : clazz.getAlternatives()) {
                if (uri.equals(alternative.getURI())) {
                    return alternative;
                }
            }
        }
        Clazz newClazz = createClazz(classImpl);
        if (clazz == null) {
            classes.put(classname, newClazz);
        } else {
            clazz.getAlternatives().add(newClazz);
        }
        return newClazz;
    }

    private Clazz resolveClazz(String classname) {
        ClassEntry resolverEntry = resolver.getClassEntry(classname);
        if (resolverEntry == null) {
            return null;
        }
        return new Clazz(this, resolverEntry);
    }

    private Clazz createClazz(Class<?> clazz) {
        try {
            return new Clazz(this, clazz);
        } catch (IOException ex) {
            throw new ResolveException("Cannot analyze " + clazz, ex);
        }
    }

    private Clazz loadClass(String classname) {
        try {
            Class<?> clazz = Class.forName(classname);
            return new Clazz(this, clazz);
        } catch (ClassNotFoundException ex) {
            log.log(Level.FINE, "Cannot find " + classname, ex);
        } catch (IOException ex) {
            log.log(Level.WARNING, "Cannot analyze " + classname, ex);
        }
        return null;
    }

    private Clazz undefined(String classname) {
        return new Clazz(this, classname);
    }

    /**
     * Returns a slice of all duplicate .class files detected by the underlying {@link ClassResolver}.
     * Hence, for each entry in this slice there are at least two .class files with the same classname but
     * different URI's.
     *
     * @return Maybe empty slice of all duplicate .class files
     */
    public ConcreteSlice duplicates() {
        Set<Clazz> sliceEntries = new HashSet<Clazz>();
        for (List<ClassEntry> alternatives : resolver.getDuplicates().values()) {
            Map<String, ClassEntry> versions = new HashMap<String, ClassEntry>();
            for (ClassEntry alternative : alternatives) {
                ClassEntry previous = versions.put(String.valueOf(alternative.getVersion()),alternative);
                if (previous != null) {
                    // only add if two entries have the same version
                    sliceEntries.add(asClazz(previous));
                    sliceEntries.add(asClazz(alternative));
                }
            }
        }
        return new ConcreteSlice(sliceEntries);
    }

    /**
     * Return the {@link Root} of some {@link Clazz}.
     *
     * @param clazz the clazz
     * @return the Root
     */
    public Root rootOf(Class<?> clazz) {
        return rootOfClass(clazz.getName());
    }

    /**
     * Return the {@link Root} of some class given by its classname.
     *
     * @param classname the fully qualified name of the class
     * @return the Root
     */
    public Root rootOfClass(String classname) {
        ClassEntry cf = resolver.getClassEntry(classname);
        if (cf == null) {
            throw new IllegalArgumentException(classname + " not found within this classpath.");
        }
        return rootOf(cf.getPackage().getRoot());
    }

    /**
     * Returns the Root for classes directory or .jar file located on the classpath.
     *
     * @param rootFile the file to get the root for
     * @return the Root
     * @throws IllegalArgumentException if <i>rootFile</i> could not be found on the classpath
     */
    public Root rootOf(File rootFile) {
        return rootOf(getClassRoot(rootFile));
    }

    Root rootOf(ClassRoot root) {
        return new Root(root, this);
    }

    /**
     * Checks whether the corresponding root file has been added to the path.
     * It's not allowed to add root files to an existing classpath, because
     * that might change slices after they have been created.
     *
     * @param rootFile the classes directory or jar file to check
     * @return the root
     */
    private ClassRoot getClassRoot(File rootFile) {
        if (rootFile == null) {
            throw new NullPointerException("rootFile must not be null");
        }
        ClassRoot root = resolver.getRoot(rootFile);
        if (root == null) {
            throw new IllegalArgumentException(rootFile + " has not been registered with this context.");
        }
        return root;
    }

    /**
     * Returns a {@link Slice} for a concrete list of Java classes.
     * The classes do not necessarily have to be on this Classpath.
     *
     * @param classes the classes
     * @return the slice
     * @see #asClazz(Class)
     */
    public Slice sliceOf(Class<?>... classes) {
        if (classes.length == 0) {
            return Slices.EMPTY_SLICE;
        } else if (classes.length == 1) {
            return asClazz(classes[0]);
        }
        Set<Clazz> clazzes = new HashSet<Clazz>();
        for (Class<?> clazz : classes) {
            clazzes.add(asClazz(clazz));
        }
        return new ConcreteSlice(clazzes);
    }

    @Override
    Classpath getClasspath() {
        return this;
    }

    @Override
    boolean isConcrete() {
        return false;
    }
}
