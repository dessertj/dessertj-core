package de.spricom.dessert.traversal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JarProcessor implements ClassProcessor {
    private static final Logger log = Logger.getLogger(JarProcessor.class.getName());

    private final File jar;

    public JarProcessor(File jar) {
        if (jar == null) {
            throw new IllegalArgumentException("jar == null");
        }
        if (!jar.isFile()) {
            throw new IllegalArgumentException("jar is not a file");
        }
        this.jar = jar;
    }

    @Override
    public void traverseAllClasses(ClassVisitor visitor) throws IOException {
        assert visitor != null : "visitor == null";
        JarFile jarFile = new JarFile(jar);
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                try {
                    process(entry, jarFile, visitor);
                } catch (IOException ex) {
                    log.log(Level.SEVERE, "Cannot process " + entry + " of " + jar, ex);
                }
            }
        }
    }

    private void process(JarEntry entry, JarFile jarFile, ClassVisitor visitor) throws IOException {
        String className = entry.getName();
        className = className.substring(0, className.length() - ".class".length());
        className = className.replace('/', '.');
        InputStream is = jarFile.getInputStream(entry);
        try {
            visitor.visit(jar, className, is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
