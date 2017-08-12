package de.spricom.dessert.slicing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.spricom.dessert.resolve.ClassFileEntry;
import de.spricom.dessert.resolve.ClassPackage;
import de.spricom.dessert.resolve.ClassResolver;

public class SliceContext {
    private static ClassResolver defaultResolver;

    private final ClassResolver resolver;
    private Map<SliceEntry, SliceEntry> entries = new HashMap<>();

    public SliceContext() throws IOException {
        if (defaultResolver == null) {
            defaultResolver = new ClassResolver();
        }
        resolver = defaultResolver;
    }

    public SliceContext(String path) throws IOException {
        resolver = new ClassResolver(path);
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

    SliceEntry getSliceEntry(String classname) {
        ClassFileEntry cf = resolver.getClassFile(classname);
        Objects.requireNonNull(cf, "No " + classname + " found!");
        return uniqueEntry(cf);
    }

    SliceEntry uniqueEntry(ClassFileEntry cf) {
        SliceEntry se = new SliceEntry(this, cf);
        SliceEntry existing = entries.get(se);
        if (existing != null) {
            return existing;
        }
        entries.put(se, se);
        return se;
    }
}
