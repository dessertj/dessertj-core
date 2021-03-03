package de.spricom.dessert.slicing;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
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

import de.spricom.dessert.resolve.ClassEntry;
import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.resolve.ClassRoot;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Classpath extends AbstractRootSlice {
    private static final Logger log = Logger.getLogger(Classpath.class.getName());
    private static ClassResolver defaultResolver;

    private final ClassResolver resolver;

    private final Map<String, Clazz> classes = new HashMap<String, Clazz>();

    public Classpath() {
        this(getDefaultResolver());
    }

    public Classpath(ClassResolver resolver) {
        super(resolver);
        this.resolver = resolver;
        resolver.freeze();
    }

    private static ClassResolver getDefaultResolver() {
        if (defaultResolver == null) {
            try {
                defaultResolver = ClassResolver.ofClassPathAndBootClassPath();
            } catch (IOException ex) {
                throw new ResolveException("Unable to access classes on classpath.", ex);
            }
        }
        return defaultResolver;
    }

    Clazz asClazz(ClassEntry ce) {
        Clazz se = classes.get(ce.getClassname());
        if (se == null) {
            se = new Clazz(this, ce);
            classes.put(ce.getClassname(), se);
            return se;
        } else {
            Clazz alt = se.getAlternative(ce);
            return alt;
        }
    }

    public Clazz asClazz(String classname) {
        Clazz se = classes.get(classname);
        if (se == null) {
            se = resolveClazz(classname);
            if (se == null) {
                se = loadClass(classname);
            }
            if (se == null) {
                se = undefined(classname);
            }
            classes.put(classname, se);
        }
        return se;
    }

    public Clazz asClazz(Class<?> clazz) {
        Clazz se = classes.get(clazz.getName());
        if (se == null) {
            se = createClazz(clazz);
            classes.put(clazz.getName(), se);
        }
        return se;
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
     * Hence for each entry in this slice there are at least two .class files with the same classname but
     * different URI's.
     *
     * @return Maybe empty slice of all duplicate .class files
     */
    public ConcreteSlice duplicates() {
        Set<Clazz> sliceEntries = new HashSet<Clazz>();
        for (List<ClassEntry> alternatives : resolver.getDuplicates().values()) {
            for (ClassEntry alternative : alternatives) {
                sliceEntries.add(asClazz(alternative));
            }
        }
        return new ConcreteSlice(sliceEntries);
    }

    public Root rootOf(Class<?> clazz) {
        return rootOfClass(clazz.getName());
    }

    public Root rootOfClass(String classname) {
        ClassEntry cf = resolver.getClassEntry(classname);
        if (cf == null) {
            throw new IllegalArgumentException(classname + " not found within this classpath.");
        }
        return rootOf(cf.getPackage().getRoot());
    }

    public Root rootOf(final File rootFile) {
        return rootOf(getClassRoot(rootFile));
    }

    private Root rootOf(final ClassRoot root) {
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

    public Slice sliceOf(final String... classnames) {
        if (classnames.length == 0) {
            return Slices.EMPTY_SLICE;
        } else if (classnames.length == 1) {
            return asClazz(classnames[0]);
        }
        Set<Clazz> clazzes = new HashSet<Clazz>();
        for (String classname : classnames) {
            clazzes.add(asClazz(classname));
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
