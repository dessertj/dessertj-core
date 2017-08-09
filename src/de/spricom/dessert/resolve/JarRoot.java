package de.spricom.dessert.resolve;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarRoot extends ClassRoot {
    public JarRoot(File file) {
        super(file);
    }

    @Override
    public boolean resolve(String packagename) throws IOException {
        if (getFirstChild() == null) {
            try (JarFile jarFile = new JarFile(getRootFile())) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.isDirectory()) {
                        add(entry.getName());
                    }
                }
            }
        }
        return true;
    }

    private void add(String name) {
        ClassContainer cc = this;
        for (String segment : name.split("/")) {
            if (cc.getFirstChild() == null) {
                new ClassPackage(cc, segment);
                cc = cc.getFirstChild();
            } else {
                ClassPackage cp = cc.getFirstChild();
                while (!segment.equals(cp.getName()) && cp.getNextSibling() != null) {
                    cp = cp.getNextSibling();
                }
                if (segment.equals(cp.getName())) {
                    cc = cp;
                } else {
                    cp.setNextSibling(new ClassPackage(cc, segment));
                    cc = cp.getNextSibling();
                }
            }
        }
    }
}
