package de.spricom.dessert.resolve;

import de.spricom.dessert.classfile.ClassFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

final class DirectoryClassEntry extends ClassEntry {
    private final File classFile;

    DirectoryClassEntry(ClassContainer pckg, File classFile) {
        super(pckg.getPackageName() + "." + simpleName(classFile), pckg);
        this.classFile = classFile;
    }

    private static String simpleName(File classFile) {
        return classFile.getName().substring(0, classFile.getName().length() - ".class".length());
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
