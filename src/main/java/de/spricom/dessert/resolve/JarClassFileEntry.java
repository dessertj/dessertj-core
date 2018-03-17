package de.spricom.dessert.resolve;

import de.spricom.dessert.classfile.ClassFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

final class JarClassFileEntry extends ClassFileEntry{
    private final JarFile jarFile;
    private final JarEntry jarEntry;

    JarClassFileEntry(ClassContainer pckg, JarFile jarFile, JarEntry jarEntry) {
        super(pckg);
        this.jarFile = jarFile;
        this.jarEntry = jarEntry;
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
