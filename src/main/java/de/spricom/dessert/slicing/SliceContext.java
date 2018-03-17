package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.*;
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
        ClassFileEntry resolverEntry = resolver.getClassFile(classname);
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
        }, new SubTreeEntryResolver(this, resolver, packageName),
                packageName + ".**");
    }

    private ConcreteSlice materialized(ClassPackage cp) {
        ConcreteSlice ss = new ConcreteSlice();
        ss.addRecursive(cp, this);
        while (cp.getNextAlternative() != null) {
            cp = cp.getNextAlternative();
            ss.addRecursive(cp, this);
        }
        return ss;
    }

    public ConcreteSlice packagesOf(Set<File> rootFiles) {
        ConcreteSlice ss = new ConcreteSlice();
        for (File rootFile : rootFiles) {
            ClassContainer cr = resolver.getRoot(rootFile);
            if (cr != null) {
                ss.addRecursive(cr, this);
            }
        }
        return ss;
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
