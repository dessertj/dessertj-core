package de.spricom.dessert.resolve;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class ClassResolver {
    private static Logger log = Logger.getLogger(ClassResolver.class.getName());

    public List<ClassRoot> path = new ArrayList<>();

    public ClassResolver() throws IOException {
        addClassPath();
        addBootClassPath();
    }

    public ClassResolver(String path) throws IOException {
        add(path);
    }

    public void addClassPath() throws IOException {
        add(System.getProperty("java.class.path"));
    }

    public void addBootClassPath() throws IOException {
        add(System.getProperty("sun.boot.class.path"));
    }

    public void add(String path) throws IOException {
        for (String entry : path.split(File.pathSeparator)) {
            addFile(entry);
        }
    }

    private void addFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            log.warning("Does not exist: " + filename);
        } else if (getRoot(file) != null) {
            log.warning("Already on path: " + filename);
        } else if (file.isDirectory()) {
            path.add(new DirectoryRoot(file));
        } else if (file.isFile() && file.getName().endsWith(".jar")) {
            path.add(new JarRoot(file));
        } else {
            log.warning("Don't know how to process: " + filename);
        }
    }

    private ClassRoot getRoot(File file) {
        for (ClassRoot root : path) {
            if (root.getRootFile().equals(file)) {
                return root;
            }
        }
        return null;
    }

    public List<ClassFileEntry> getClassFile(String classname) {
        return null;
    }

    public List<ClassPackage> getPackage(String packagename) throws IOException {
        List<ClassPackage> matches = new ArrayList<>();
        for (ClassRoot cr : path) {
            ClassPackage match = getPackage(cr, packagename);
            if (match != null) {
                matches.add(match);
            }
        }
        return matches;
    }

    public ClassFileEntry getClassFile(File root, String classname) {
        return null;
    }

    public ClassPackage getPackage(File root, String packagename) throws IOException {
        ClassRoot cr = getRoot(root);
        Objects.requireNonNull(cr, root + " is not in path");
        return getPackage(cr, packagename);
    }

    private ClassPackage getPackage(ClassRoot cr, String packagename) throws IOException {
        return cr.packages.get(packagename);
    }

    public List<ClassPackage> getSubpackages(ClassContainer pckg) {
        return null;
    }

    public List<ClassFileEntry> getClasses(ClassContainer pckg) {
        return null;
    }
}
