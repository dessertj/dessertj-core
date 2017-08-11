package de.spricom.dessert.resolve;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarRoot extends ClassRoot {
    public JarRoot(File file) throws IOException {
        super(file);
        try (JarFile jarFile = new JarFile(getRootFile())) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    int index = entry.getName().lastIndexOf('/');
                    if (index != -1) {
                        String pn = entry.getName().substring(0, index).replace('/', '.');
                        if (!packages.containsKey(pn)) {
                            new ClassPackage(this, pn);
                        }
                    }
                }
             }
        }
    }
}
