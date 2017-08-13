package de.spricom.dessert.slicing;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.spricom.dessert.resolve.ClassFileEntry;
import de.spricom.dessert.resolve.ClassPackage;
import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.resolve.ClassRoot;

public class SliceContext {
    private static Logger log = Logger.getLogger(SliceContext.class.getName());
    private static ClassResolver defaultResolver;

    private final ClassResolver resolver;
    private boolean useClassLoader = true;

    private Map<String, SliceEntry> entries = new HashMap<>();

    public SliceContext() throws IOException {
        if (defaultResolver == null) {
            defaultResolver = ClassResolver.ofClassPath();
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

    public SliceSet subPackagesOf(Package pkg) {
        return subPackagesOf(pkg.getName());
    }

    public SliceSet subPackagesOf(String packageName) {
        SliceSet ss = new SliceSet();
        ClassPackage cp = resolver.getPackage(packageName);
        if (cp != null) {
            ss.addRecursive(cp, this);
            while (cp.getAlternative() != null) {
                cp = cp.getAlternative();
                ss.addRecursive(cp, this);
            }
        }
        return ss;
    }

    public SliceSet packagesOf(Set<File> rootFiles) {
        SliceSet ss = new SliceSet();
        for (File rootFile : rootFiles) {
            ClassRoot cr = resolver.getRoot(rootFile);
            if (cr != null) {
                ss.addRecursive(cr, this);
            }
        }
        return ss;
    }
    
    public Slice packageOf(String packageName) {
        ClassPackage cp = resolver.getPackage(packageName);
        if (cp == null) {
            return null;
        }
        return new Slice(cp, this);
    }
    
    public boolean isUseClassLoader() {
        return useClassLoader;
    }

    public void setUseClassLoader(boolean useClassLoader) {
        this.useClassLoader = useClassLoader;
    }
}
