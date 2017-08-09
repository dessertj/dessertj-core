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
    
    public void add(String filename) {
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

    public List<ClassPackage> getPackage(String packagename) {
        return null;
    }

    public ClassFileEntry getClassFile(File root, String classname) {
        return null;
    }

    public ClassPackage getPackage(File root, String packagename) throws IOException {
        ClassRoot cr = getRoot(root);
        Objects.requireNonNull(cr, root + " is not in path");
        
        ClassContainer cc = cr;
        int lastIndex = 0;
        int index = packagename.indexOf('.');
        while (cr != null && index != -1) {
            String segment = packagename.substring(lastIndex, index);
            if (cc.getFirstChild() == null) {
                cr.resolve(packagename);
            }
            cc = cc.find(segment);
            lastIndex = index + 1;
        }
        if (cc != null) {
            String segment = packagename.substring(lastIndex);
            cc = cc.find(segment);
        }
        return (ClassPackage) cc;
    }

    public List<ClassPackage> getSubpackages(ClassContainer pckg) {
        return null;
    }

    public List<ClassFileEntry> getClasses(ClassContainer pckg) {
        return null;
    }
}
