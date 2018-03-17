package de.spricom.dessert.resolve;

import de.spricom.dessert.classfile.ClassFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

final class DirectoryClassFileEntry extends ClassFileEntry{
    private final File classFile;

    DirectoryClassFileEntry(ClassContainer pckg, File classFile) {
        super(pckg);
        this.classFile = classFile;
    }

    @Override
    public ClassFile resolveClassFile() {
        InputStream is = null;
        try {
            is = new FileInputStream(classFile);
            ClassFile cf = new ClassFile(is);
            return cf;
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to read " + classFile.getAbsolutePath(), ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    throw new IllegalStateException("Cannot close stream after reading " + classFile.getAbsolutePath(), ex);
                }
            }
        }
    }
}
