package de.spricom.dessert.resolve;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

public class ClassResolver {
    private static Logger log = Logger.getLogger(ClassResolver.class.getName());

    public List<ClassRoot> path = new ArrayList<>();
    public final Map<String, ClassPackage> packages = new TreeMap<>();

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
            path.add(new DirectoryRoot(this, file));
        } else if (file.isFile() && file.getName().endsWith(".jar")) {
            path.add(new JarRoot(this, file));
        } else {
            log.warning("Don't know how to process: " + filename);
        }
    }

    void addPackage(ClassPackage cp) {
        ClassPackage previous = packages.put(cp.getPackageName(), cp);
        assert previous == null : "Added " + cp + " twice!";
    }

    private ClassRoot getRoot(File file) {
        for (ClassRoot root : path) {
            if (root.getRootFile().equals(file)) {
                return root;
            }
        }
        return null;
    }

    public ClassPackage getPackage(String packageName) {
        return packages.get(packageName);
    }

    public ClassPackage getPackage(File root, String packageName) {
        ClassPackage cp = getPackage(packageName);
        while (cp != null && !root.equals(cp.getRootFile())) {
            cp = cp.getAlternative();
        }
        return cp;
    }

    public ClassFileEntry getClassFile(File root, String classname) {
        return null;
    }

    public ClassFileEntry getClassFile(String classname) {
        return null;
    }
}
