package de.spricom.dessert.slicing;

import java.io.File;
import java.util.function.Predicate;

import de.spricom.dessert.dependency.ClassFileEntry;

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

    public Slice getParentPackage() {
        return new Slice();
    }

    public File getRoot() {
        return new File(".");
    }
    
    public Slice slice(Predicate<ClassFileEntry> predicate) {
        return new Slice();
    }

}
