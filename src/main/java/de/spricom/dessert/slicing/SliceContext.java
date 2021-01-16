package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.ClassEntry;
import de.spricom.dessert.resolve.ClassPackage;
import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.resolve.ClassRoot;
import de.spricom.dessert.util.Predicate;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SliceContext {
    private static Logger log = Logger.getLogger(SliceContext.class.getName());
    private static ClassResolver defaultResolver;

    private final ClassResolver resolver;
    private boolean useClassLoader = true;

    private Map<String, Clazz> entries = new HashMap<String, Clazz>();

    public SliceContext() {
        this(getDefaultResolver());
    }

    public SliceContext(ClassResolver resolver) {
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
        Clazz se = entries.get(ce.getClassname());
        if (se == null) {
            se = new Clazz(this, ce);
            entries.put(ce.getClassname(), se);
            return se;
        } else {
            Clazz alt = se.getAlternative(ce);
            return alt;
        }
    }

    public Clazz asClazz(String classname) {
        Clazz se = entries.get(classname);
        if (se == null) {
            se = resolveEntry(classname);
            if (se == null && useClassLoader) {
                se = loadClass(classname);
            }
            if (se == null) {
                se = undefined(classname);
            }
            entries.put(classname, se);
        }
        return se;
    }

    public Clazz asClazz(Class<?> clazz) {
        Clazz se = entries.get(clazz.getName());
        if (se == null) {
            se = createEntry(clazz);
            entries.put(clazz.getName(), se);
        }
        return se;
    }

    private Clazz resolveEntry(String classname) {
        ClassEntry resolverEntry = resolver.getClassEntry(classname);
        if (resolverEntry == null) {
            return null;
        }
        return new Clazz(this, resolverEntry);
    }

    private Clazz createEntry(Class<?> clazz) {
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

    public Slice packageTreeOf(Class<?> clazz) {
        return packageTreeOf(clazz.getPackage());
    }

    public Slice packageTreeOf(Package pkg) {
        return packageTreeOf(pkg.getName());
    }

    public Slice packageTreeOf(final String packageName) {
        DerivedSlice derivedSlice = new DerivedSlice(new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz sliceEntry) {
                return sliceEntry.getName().startsWith(packageName);
            }
        });
        return new DeferredSlice(derivedSlice, new AbstractTreeResolver(this) {
            @Override
            protected void resolve() {
                ClassPackage cp = resolver.getPackage(packageName);
                if (cp == null) {
                    throw new ResolveException("Cannot resolve " + packageName);
                }
                addRecursiveWithAlternatives(cp);
            }
        });
    }

    public Slice packageOf(Class<?> clazz) {
        return packageOf(clazz.getPackage());
    }

    public Slice packageOf(Package pkg) {
        return packageOf(pkg.getName());
    }

    public Slice packageOf(final String packageName) {
        DerivedSlice derivedSlice = new DerivedSlice(new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz sliceEntry) {
                return sliceEntry.getName().startsWith(packageName);
            }
        });
        return new DeferredSlice(derivedSlice, new AbstractTreeResolver(this) {
            @Override
            protected void resolve() {
                ClassPackage cp = resolver.getPackage(packageName);
                if (cp == null) {
                    throw new ResolveException("Cannot resolve " + packageName);
                }
                addWithAlternatives(cp);
            }
        });
    }

    public Slice packageTreeOf(File root, Class<?> clazz) {
        return packageTreeOf(root, clazz.getPackage());
    }

    public Slice packageTreeOf(File root, Package pkg) {
        return packageTreeOf(root, pkg.getName());
    }

    public Slice packageTreeOf(final File root, final String packageName) {
        ensureRootFile(root);
        DerivedSlice derivedSlice = new DerivedSlice(new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz sliceEntry) {
                return sliceEntry.getName().startsWith(packageName);
            }
        });
        return new DeferredSlice(derivedSlice, new AbstractTreeResolver(this) {
            @Override
            protected void resolve() {
                ClassPackage cp = resolver.getPackage(root, packageName);
                if (cp == null) {
                    throw new ResolveException("Cannot resolve " + packageName);
                }
                addRecursive(cp);
            }
        });
    }

    public Slice packageOf(File root, Class<?> clazz) {
        return packageOf(root, clazz.getPackage());
    }

    public Slice packageOf(File root, Package pkg) {
        return packageOf(root, pkg.getName());
    }

    public Slice packageOf(File root, String packageName) {
        ensureRootFile(root);
        final ClassPackage cp = SliceContext.this.resolver.getPackage(root, packageName);
        if (cp == null) {
            throw new ResolveException("There is no " + packageName + " package in " + root.getAbsolutePath());
        }
        AbstractTreeResolver resolver = new AbstractTreeResolver(this) {
            @Override
            protected void resolve() {
                addRecursive(cp);
            }
        };
        return new ConcreteSlice(resolver.getSliceEntries());
    }

    public Slice packagesOf(File... rootFiles) {
        return packagesOf(Arrays.asList(rootFiles));
    }

    public Slice packagesOf(final Collection<File> rootFiles) {
        AbstractTreeResolver resolver = new AbstractTreeResolver(this) {
            @Override
            protected void resolve() {
                for (File file : rootFiles) {
                    ensureRootFile(file);
                    ClassRoot cr = SliceContext.this.resolver.getRoot(file);
                    if (cr != null) {
                        addRecursive(cr);
                    }
                }
            }
        };
        return new ConcreteSlice(resolver.getSliceEntries());
    }

    public Slice sliceOf(Class<?>... classes) {
        if (classes.length == 0) {
            return Slices.EMPTY_SLICE;
        } else if (classes.length == 1) {
            return asClazz(classes[0]);
        }
        Set<Clazz> sliceEntries = new HashSet<Clazz>();
        for (Class<?> clazz : classes) {
            sliceEntries.add(asClazz(clazz));
        }
        return new ConcreteSlice(sliceEntries);
    }

    public Slice sliceOf(final String... classnames) {
        DerivedSlice derivedSlice = new DerivedSlice(new Predicate<Clazz>() {
            private final Set<String> names = new HashSet<String>(Arrays.asList(classnames));

            @Override
            public boolean test(Clazz sliceEntry) {
                return names.contains(sliceEntry.getName());
            }
        });
        return new DeferredSlice(derivedSlice, new EntryResolver() {
            @Override
            public Set<Clazz> getSliceEntries() {
                Set<Clazz> sliceEntries = new HashSet<Clazz>();
                for (String name : classnames) {
                    sliceEntries.add(asClazz(name));
                }
                return sliceEntries;
            }
        });
    }

    /**
     * Returns a slice of all duplicate .class files detected by the underlying {@link ClassResolver}.
     * Hence for each entry in this slice there are at least two .class files with the same classname but
     * different URL's.
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

    /**
     * Checks whether the correspondig root file has been added to the path.
     * It's not allowed to add root files to an existing slice context, because
     * that might change slices after they have been created.
     *
     * @param rootFile the classes directory or jar file to check
     */
    private void ensureRootFile(File rootFile) {
        if (rootFile == null) {
            throw new NullPointerException("rootFile must not be null");
        }
        if (resolver.getRoot(rootFile) == null) {
            throw new IllegalArgumentException(rootFile + " has not been registered with this context.");
        }
    }

    public File rootOf(String classname) {
        ClassEntry cf = resolver.getClassEntry(classname);
        if (cf == null) {
            return null;
        }
        return cf.getPackage().getRootFile();
    }

    public File rootOf(Class<?> clazz) {
        return rootOf(clazz.getName());
    }

    public boolean isUseClassLoader() {
        return useClassLoader;
    }

    public void setUseClassLoader(boolean useClassLoader) {
        this.useClassLoader = useClassLoader;
    }
}
