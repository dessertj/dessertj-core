package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.ClassEntry;
import de.spricom.dessert.resolve.ClassPackage;
import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.resolve.ClassRoot;
import de.spricom.dessert.util.Predicate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SliceContext {
    private static Logger log = Logger.getLogger(SliceContext.class.getName());
    private static ClassResolver defaultResolver;

    private final ClassResolver resolver;
    private boolean useClassLoader = true;

    private Map<String, SliceEntry> entries = new HashMap<String, SliceEntry>();

    public SliceContext() throws IOException {
        if (defaultResolver == null) {
            defaultResolver = ClassResolver.ofClassPathAndBootClassPath();
        }
        resolver = defaultResolver;
    }

    public SliceContext(ClassResolver resolver) throws IOException {
        this.resolver = resolver;
    }

    SliceEntry getSliceEntry(ClassEntry ce) {
        SliceEntry se = entries.get(ce.getClassname());
        if (se == null) {
            se = new SliceEntry(this, ce);
            entries.put(ce.getClassname(), se);
        } else {
            SliceEntry alt = se.getAlternative(ce);
            if (alt == null) {
                alt = new SliceEntry(this, ce);
                alt.addAlternative(se);
            }
            se = alt;
        }
        return se;
    }

    SliceEntry getSliceEntry(String classname) {
        SliceEntry se = entries.get(classname);
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

    private SliceEntry getSliceEntry(Class<?> clazz) {
        SliceEntry se = entries.get(clazz.getName());
        if (se == null) {
            se = createEntry(clazz);
            entries.put(clazz.getName(), se);
        }
        return se;
    }

    private SliceEntry resolveEntry(String classname) {
        ClassEntry resolverEntry = resolver.getClassEntry(classname);
        if (resolverEntry == null) {
            return null;
        }
        return new SliceEntry(this, resolverEntry);
    }

    private SliceEntry createEntry(Class<?> clazz) {
        try {
            return new SliceEntry(this, clazz);
        } catch (IOException ex) {
            throw new ResolveException("Cannot analyze " + clazz, ex);
        }
    }

    private SliceEntry loadClass(String classname) {
        try {
            Class<?> clazz = Class.forName(classname);
            return new SliceEntry(this, clazz);
        } catch (ClassNotFoundException ex) {
            log.log(Level.FINE, "Cannot find " + classname, ex);
        } catch (IOException ex) {
            log.log(Level.WARNING, "Cannot analyze " + classname, ex);
        }
        return null;
    }

    private SliceEntry undefined(String classname) {
        return new SliceEntry(this, classname);
    }

    public Slice packageTreeOf(Class<?> clazz) {
        return subPackagesOf(clazz.getPackage());
    }

    public Slice subPackagesOf(Package pkg) {
        return subPackagesOf(pkg.getName());
    }

    public Slice subPackagesOf(final String packageName) {
        return new DeferredSlice(new Predicate<SliceEntry>() {
            @Override
            public boolean test(SliceEntry sliceEntry) {
                return sliceEntry.getClassname().startsWith(packageName);
            }
        }, new AbstractTreeResolver(this) {
            @Override
            protected void resolve() {
                ClassPackage cp = resolver.getPackage(packageName);
                if (cp == null) {
                    throw new IllegalStateException("Cannot resolve " + packageName);
                }
                addRecursiveWithAlternatives(cp);
            }
        },
                packageName + ".**");
    }

    public Slice packagesOf(final Set<File> rootFiles) {
        StringBuilder sb = new StringBuilder();
        for (File file : rootFiles) {
            if (sb.length() != 0) {
                sb.append(File.pathSeparator);
            }
            sb.append(file.getPath());
        }
        AbstractTreeResolver resolver = new AbstractTreeResolver(this) {
            @Override
            protected void resolve() {
                for (File file : rootFiles) {
                    ClassRoot cr = SliceContext.this.resolver.getRoot(file);
                    if (cr != null) {
                        addRecursive(cr);
                    }
                }
            }
        };
        return new ConcreteSlice(resolver.getSliceEntries());
    }

    public ConcreteSlice sliceOf(Class<?>... classes) {
        Set<SliceEntry> sliceEntries = new HashSet<SliceEntry>();
        for (Class<?> clazz : classes) {
            sliceEntries.add(getSliceEntry(clazz));
        }
        return new ConcreteSlice(sliceEntries);
    }

    public boolean isUseClassLoader() {
        return useClassLoader;
    }

    public void setUseClassLoader(boolean useClassLoader) {
        this.useClassLoader = useClassLoader;
    }
}
