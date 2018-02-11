package de.spricom.dessert.traversal;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PathProcessor implements ClassProcessor {
    private static Logger log = Logger.getLogger(PathProcessor.class.getName());

    private String path;

    public PathProcessor() {
        path = System.getProperty("java.class.path");
    }

    @Override
    public void traverseAllClasses(ClassVisitor visitor) throws IOException {
        for (String file : path.split(File.pathSeparator)) {
            process(file, visitor);
        }
    }

    private void process(String filename, ClassVisitor visitor) {
        File file = new File(filename);
        if (!filter(file)) {
            return;
        }
        if (!file.exists()) {
            log.warning("Does not exist: " + filename);
        } else if (file.isDirectory()) {
            try {
                processDirectory(file, visitor);
            } catch (IOException ex) {
                log.log(Level.WARNING, "Cannot process: " + file, ex);
            }
        } else if (file.isFile() && file.getName().endsWith(".jar")) {
            try {
                processJar(file, visitor);
            } catch (IOException ex) {
                log.log(Level.WARNING, "Cannot process: " + file, ex);
            }
        } else {
            log.warning("Don't know how to process: " + filename);
        }
    }

    protected boolean filter(File file) {
        return true;
    }

    protected void processJar(File file, ClassVisitor visitor) throws IOException {
        new JarProcessor(file).traverseAllClasses(visitor);
    }

    protected void processDirectory(File file, ClassVisitor visitor) throws IOException {
        new DirectoryProcessor(file).traverseAllClasses(visitor);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
