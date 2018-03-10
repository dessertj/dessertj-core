package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.*;
import de.spricom.dessert.util.Predicate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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

    private SliceEntry resolveEntry(String classname) {
        ClassFileEntry resolverEntry = resolver.getClassFile(classname);
        if (resolverEntry == null) {
            return null;
        }
        return new SliceEntry(this, resolverEntry);
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

    public Slice subPackagesOf(Package pkg) {
        return subPackagesOf(pkg.getName());
    }

    public Slice subPackagesOf(final String packageName) {
        ClassPackage cp = resolver.getPackage(packageName);
        if (cp != null) {
            return materialized(cp);
        }
        return new DerivedSlice(new Predicate<SliceEntry>() {
            @Override
            public boolean test(SliceEntry sliceEntry) {
                return sliceEntry.getClassname().startsWith(packageName);
            }
        });
    }

    private ConcreteSlice materialized(ClassPackage cp) {
        ConcreteSlice ss = new ConcreteSlice();
        ss.addRecursive(cp, this);
        while (cp.getAlternative() != null) {
            cp = cp.getAlternative();
            ss.addRecursive(cp, this);
        }
        return ss;
    }

    public ConcreteSlice packagesOf(Set<File> rootFiles) {
        ConcreteSlice ss = new ConcreteSlice();
        for (File rootFile : rootFiles) {
            ClassRoot cr = resolver.getRoot(rootFile);
            if (cr != null) {
                ss.addRecursive(cr, this);
            }
        }
        return ss;
    }

    private PackageSlice packageOf(String packageName) {
        ClassPackage cp = resolver.getPackage(packageName);
        if (cp == null) {
            return null;
        }
        return new PackageSlice(cp, this);
    }

    public boolean isUseClassLoader() {
        return useClassLoader;
    }

    public void setUseClassLoader(boolean useClassLoader) {
        this.useClassLoader = useClassLoader;
    }
}
