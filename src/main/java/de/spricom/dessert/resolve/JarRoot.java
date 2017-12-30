package de.spricom.dessert.resolve;

import de.spricom.dessert.classfile.ClassFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarRoot extends ClassRoot {
    public JarRoot(ClassResolver resolver, File file) throws IOException {
        super(resolver, file);
        JarFile jarFile = new JarFile(getRootFile());
        try {
            Enumeration<JarEntry> entries = jarFile.entries();
            String lastPackageName = "";
            ClassPackage lastPackage = null;
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    int index = entry.getName().lastIndexOf('/');
                    if (index == -1) {
                        addClass(this, entry.getName(), entry, jarFile);
                    } else if (index == lastPackageName.length() && entry.getName().startsWith(lastPackageName)) {
                        addClass(lastPackage, entry.getName().substring(index + 1), entry, jarFile);
                    } else {
                        lastPackageName = entry.getName().substring(0, index);
                        lastPackage = addPackage(lastPackageName.replace('/', '.'));
                        addClass(lastPackage, entry.getName().substring(index + 1), entry, jarFile);
                    }
                }
            }
        } finally {
            if (jarFile != null) {
                jarFile.close();
            }
        }
    }

    private void addClass(ClassContainer cc, String filename, JarEntry entry, JarFile jarFile) throws IOException {
        if (cc.getClasses() == null) {
            cc.setClasses(new LinkedList<ClassFileEntry>());
        }
        InputStream is = jarFile.getInputStream(entry);
        try {
            ClassFile cf = new ClassFile(is);
            ClassFileEntry cfe = new ClassFileEntry(cc, filename, cf);
            addClass(cfe);
            cc.getClasses().add(cfe);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
