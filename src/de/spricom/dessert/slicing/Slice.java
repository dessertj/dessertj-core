package de.spricom.dessert.slicing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import de.spricom.dessert.resolve.ClassContainer;
import de.spricom.dessert.resolve.ClassFileEntry;
import de.spricom.dessert.resolve.ClassPackage;

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
    private final List<SliceEntry> entries;
    
    Slice(ClassContainer cc, SliceContext context) {
        container = cc;
        this.context = context;
        entries = new ArrayList<>(container.getClasses().size());
        for (ClassFileEntry cf : container.getClasses()) {
            entries.add(context.uniqueEntry(cf));
        }
    }
    
    private Slice(Slice slice, Predicate<SliceEntry> predicate) {
        container = slice.container;
        context = slice.context;
        entries = new ArrayList<>(slice.entries.size());
        for (SliceEntry entry : slice.entries) {
            if (predicate.test(entry)) {
                entries.add(entry);
            }
        }
    }
    
    public Slice getParentPackage() {
        if (container instanceof ClassPackage) {
            return new Slice(((ClassPackage)container).getParent(), context);
        }
        return null;
    }

    public List<SliceEntry> getEntries() {
        return entries;
    }

    public Slice slice(Predicate<SliceEntry> predicate) {
        return new Slice(this, predicate);
    }

    public String getPackageName() {
        return container.getPackageName();
    }

    public File getRootFile() {
        return container.getRootFile();
    }
}
