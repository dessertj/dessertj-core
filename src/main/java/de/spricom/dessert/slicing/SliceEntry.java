package de.spricom.dessert.slicing;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.resolve.ClassFileEntry;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * For each class belonging to a {@link PackageSlice} there is a SliceEntry.
 */
public final class SliceEntry {
    public static final SliceEntry UNDEFINED = new SliceEntry();

    private final SliceContext context;
    private final String classname;
    private final ClassFile classfile;
    private final ClassFileEntry resolverEntry;
    private Class<?> clazz;

    private SliceEntry superclass;
    private List<SliceEntry> implementedInterfaces;
    private Set<SliceEntry> usedClasses;
    private Set<SliceEntry> alternatives;

    private SliceEntry() {
        context = null;
        classname = "undefined";
        classfile = null;
        resolverEntry = null;
        superclass = this;
        implementedInterfaces = Collections.emptyList();
        usedClasses = Collections.emptySet();
        alternatives = Collections.emptySet();
    }

    SliceEntry(SliceContext context, ClassFileEntry resolverEntry) {
        assert context != null : "context == null";
        assert resolverEntry != null : "resolverEntry == null";
        this.context = context;
        this.resolverEntry = resolverEntry;
        this.classfile = resolverEntry.getClassfile();
        this.classname = classfile.getThisClass();
    }

    SliceEntry(SliceContext context, Class<?> clazz) throws IOException {
        assert context != null : "context == null";
        assert clazz != null : "clazz == null";
        this.context = context;
        this.clazz = clazz;
        this.resolverEntry = null;
        this.classfile = new ClassFile(clazz);
        this.classname = classfile.getThisClass();
    }

    SliceEntry(SliceContext context, String classname) {
        assert context != null : "context == null";
        assert classname != null : "classname == null";
        this.context = context;
        this.resolverEntry = null;
        this.classfile = null;
        this.classname = classname;
        superclass = UNDEFINED;
        implementedInterfaces = Collections.emptyList();
        usedClasses = Collections.emptySet();
        alternatives = Collections.emptySet();
    }

    public File getRootFile() {
        if (resolverEntry != null) {
            return resolverEntry.getPackage().getRootFile();
        } else if (clazz != null) {
            return getRootFile(clazz);
        } else {
            return null;
        }
    }

    public static final File getRootFile(Class<?> clazz) {
        String filename = "/" + clazz.getName().replace('.', '/') + ".class";
        URL url = clazz.getResource(filename);
        assert url != null : "Resource " + filename + " not found!";
        if ("file".equals(url.getProtocol())) {
            assert url.getFile().endsWith(filename) : url + " does not end with " + filename;
            return new File(url.getFile().substring(0, url.getFile().length() - filename.length()));
        } else if ("jar".equals(url.getProtocol())) {
            assert url.getFile().startsWith("file:") : url + " does not start with jar:file";
            assert url.getFile().endsWith(".jar!" + filename) : url + " does not end with .jar!" + filename;
            try {
                return new File(URLDecoder.decode(url.getFile().substring("file:".length(), url.getFile().length() - filename.length() - 1), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                throw new IllegalStateException("UTF-8 encoding not supported!", ex);
            }
        } else {
            throw new IllegalArgumentException("Unknown protocol in " + url);
        }
    }

    public String getPackageName() {
        if (resolverEntry != null) {
            return resolverEntry.getPackage().getPackageName();
        } else if (clazz != null) {
            return clazz.getPackage().getName();
        } else {
            int index = classname.lastIndexOf('.');
            if (index == -1) {
                return "";
            }
            return classname.substring(0, index);
        }
    }

    public String getFilename() {
        if (resolverEntry != null) {
            return resolverEntry.getFilename();
        } else if (clazz != null) {
            return clazz.getSimpleName() + ".class";
        } else {
            return classname.substring(classname.lastIndexOf('.') + 1);
        }
    }

    @Override
    public int hashCode() {
        return classname.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SliceEntry other = (SliceEntry) obj;
        if (!classname.equals(other.classname)) {
            return false;
        }
        if (!(getRootFile() == other.getRootFile()
                || getRootFile() != null && getRootFile().equals(other.getRootFile()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return classname;
    }

    public boolean isUndefined() {
        return classfile == null;
    }

    public Class<?> getClazz() throws ClassNotFoundException {
        if (clazz == null && this != UNDEFINED) {
            clazz = Class.forName(classname);
        }
        return clazz;
    }

    public SliceEntry getSuperclass() {
        if (superclass == null) {
            superclass = context.getSliceEntry(resolverEntry.getClassfile().getSuperClass());
        }
        return superclass;
    }

    public List<SliceEntry> getImplementedInterfaces() {
        if (implementedInterfaces == null) {
            implementedInterfaces = new ArrayList<SliceEntry>(resolverEntry.getClassfile().getInterfaces().length);
            for (String in : resolverEntry.getClassfile().getInterfaces()) {
                implementedInterfaces.add(context.getSliceEntry(in));
            }
        }
        return implementedInterfaces;
    }

    public Set<SliceEntry> getUsedClasses() {
        if (usedClasses == null) {
            usedClasses = new HashSet<SliceEntry>(resolverEntry.getClassfile().getDependentClasses().size());
            for (String cn : resolverEntry.getClassfile().getDependentClasses()) {
                usedClasses.add(context.getSliceEntry(cn));
            }
        }
        return usedClasses;
    }

    public Set<SliceEntry> getAlternatives() {
        if (alternatives == null) {
            if (resolverEntry.getAlternatives() == null) {
                alternatives = Collections.emptySet();
            } else {
                alternatives = new HashSet<SliceEntry>(resolverEntry.getAlternatives().size());
                for (ClassFileEntry cf : resolverEntry.getAlternatives()) {
                    usedClasses.add(new SliceEntry(context, cf));
                }
            }
        }
        return alternatives;
    }

    public String getClassname() {
        return classname;
    }
}
