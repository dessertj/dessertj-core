package de.spricom.dessert.slicing;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import de.spricom.dessert.resolve.ClassContainer;
import de.spricom.dessert.resolve.ClassFileEntry;
import de.spricom.dessert.resolve.ClassPackage;
import de.spricom.dessert.resolve.ClassPredicate;

/**
 * A slice represents (subset of) a single Java package for one concrete root.
 * It's elements are .class files contained in the package. A root is either a
 * directory in the file-system or a jar file. Hence each elements of a slice is
 * unique. (There may be two classes with the same name on the classpath, but
 * with the combination of classname and root given by a Slice the class is
 * uniquely defined.) A slice may represent a subset of the .class files in a
 * package, for example all interfaces, all classes complying some naming
 * convention, all classes implementing some interfaces, all inner classes etc.
 */
public class Slice {
    private final ClassContainer container;
    private final SliceContext context;
    private final Set<SliceEntry> entries;

    Slice(ClassContainer cc, SliceContext context) {
        container = cc;
        this.context = context;
        entries = new HashSet<>(container.getClasses().size());
        for (ClassFileEntry cf : container.getClasses()) {
            entries.add(new SliceEntry(context, cf));
        }
    }

    private Slice(Slice slice, ClassPredicate<SliceEntry> predicate) {
        container = slice.container;
        context = slice.context;
        entries = new HashSet<>(slice.entries.size());
        for (SliceEntry entry : slice.entries) {
            if (predicate.test(entry)) {
                entries.add(entry);
            }
        }
    }

    public Slice getParentPackage() {
        if (container instanceof ClassPackage) {
            return new Slice(((ClassPackage) container).getParent(), context);
        }
        return null;
    }

    public Set<SliceEntry> getEntries() {
        return entries;
    }

    public Slice slice(ClassPredicate<SliceEntry> predicate) {
        return new Slice(this, predicate);
    }

    public SliceSet asSliceSet() {
        return new SliceSet(this);
    }

    public String getPackageName() {
        return container.getPackageName();
    }

    public File getRootFile() {
        return container.getRootFile();
    }
}
