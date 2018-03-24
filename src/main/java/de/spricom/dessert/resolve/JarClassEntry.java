package de.spricom.dessert.resolve;

import de.spricom.dessert.classfile.ClassFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

final class JarClassEntry extends ClassEntry {
    private final JarFile jarFile;
    private final JarEntry jarEntry;

    JarClassEntry(ClassPackage pckg, JarFile jarFile, JarEntry jarEntry) {
        super(classname(jarEntry), pckg);
        this.jarFile = jarFile;
        this.jarEntry = jarEntry;
    }

    private static String classname(JarEntry jarEntry) {
        String cn = jarEntry.getName();
        return cn.substring(0, cn.length() - ".class".length()).replace('/', '.');
    }

    @Override
    public ClassFile resolveClassFile() {
        InputStream is = null;
        try {
            is = jarFile.getInputStream(jarEntry);
            ClassFile cf = new ClassFile(is);
            return cf;
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to read " + jarEntry.getName() + " from " + jarFile.getName(), ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    throw new IllegalStateException("Cannot close stream after reading " + jarEntry.getName() + " from " + jarFile.getName(), ex);
                }
            }
        }
    }
}
