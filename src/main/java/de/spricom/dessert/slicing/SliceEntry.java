package de.spricom.dessert.slicing;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.groups.PackageSlice;
import de.spricom.dessert.resolve.ClassEntry;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.logging.Logger;

/**
 * For each class belonging to a {@link PackageSlice} there is a SliceEntry.
 */
public final class SliceEntry implements Comparable<SliceEntry> {
    private static final Logger log = Logger.getLogger(SliceEntry.class.getName());
    public static final SliceEntry UNDEFINED = new SliceEntry();

    private final SliceContext context;
    private final String classname;
    private final ClassFile classfile;
    private final ClassEntry classEntry;
    private Class<?> clazz;
    private URI uri;

    private SliceEntry superclass;
    private List<SliceEntry> implementedInterfaces;
    private Set<SliceEntry> usedClasses;
    private List<SliceEntry> alternatives;

    private SliceEntry() {
        context = null;
        classname = "undefined";
        classfile = null;
        classEntry = null;
        superclass = this;
        implementedInterfaces = Collections.emptyList();
        usedClasses = Collections.emptySet();
        alternatives = Collections.emptyList();
    }

    SliceEntry(SliceContext context, ClassEntry classEntry) {
        assert context != null : "context == null";
        assert classEntry != null : "classEntry == null";
        this.context = context;
        this.classEntry = classEntry;
        this.classfile = classEntry.getClassfile();
        this.classname = classfile.getThisClass();
        setAlternatives(classEntry);
    }

    SliceEntry(SliceContext context, Class<?> clazz) throws IOException {
        assert context != null : "context == null";
        assert clazz != null : "clazz == null";
        this.context = context;
        this.clazz = clazz;
        this.classEntry = null;
        this.classfile = new ClassFile(clazz);
        this.classname = classfile.getThisClass();
    }

    SliceEntry(SliceContext context, String classname) {
        assert context != null : "context == null";
        assert classname != null : "classname == null";
        this.context = context;
        this.classEntry = null;
        this.classfile = null;
        this.classname = classname;
        superclass = UNDEFINED;
        implementedInterfaces = Collections.emptyList();
        usedClasses = Collections.emptySet();
        alternatives = Collections.emptyList();
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

    public File getRootFile() {
        if (classEntry != null) {
            return classEntry.getPackage().getRootFile();
        } else if (clazz != null) {
            return getRootFile(clazz);
        } else {
            return null;
        }
    }

    public String getPackageName() {
        if (classEntry != null) {
            return classEntry.getPackage().getPackageName();
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
        return getURI().equals(other.getURI());
    }

    @Override
    public int compareTo(SliceEntry o) {
        return getClassname().compareTo(o.getClassname());
    }

    @Override
    public String toString() {
        return classname;
    }

    public boolean isUnknown() {
        return classfile == null;
    }

    public Class<?> getClazz() throws ClassNotFoundException {
        if (clazz == null && !isUnknown()) {
            clazz = Class.forName(classname);
            if (!getURI().equals(getURI(clazz))) {
                // TODO: Use specialized classloader to prevent this
                log.warning("Loaded class " + getURI(clazz) + " for entry " + getURI() + "!");
            }
        }
        return clazz;
    }

    public SliceEntry getSuperclass() {
        if (superclass == null && classfile != null) {
            superclass = context.getSliceEntry(classfile.getSuperClass());
        }
        return superclass;
    }

    public List<SliceEntry> getImplementedInterfaces() {
        if (implementedInterfaces == null && classfile != null) {
            implementedInterfaces = new ArrayList<SliceEntry>(classfile.getInterfaces().length);
            for (String in : classEntry.getClassfile().getInterfaces()) {
                implementedInterfaces.add(context.getSliceEntry(in));
            }
        }
        return implementedInterfaces;
    }

    public Set<SliceEntry> getUsedClasses() {
        if (usedClasses == null && classfile != null) {
            usedClasses = new HashSet<SliceEntry>(classfile.getDependentClasses().size());
            for (String cn : classfile.getDependentClasses()) {
                usedClasses.add(context.getSliceEntry(cn));
            }
        }
        return usedClasses;
    }

    public List<SliceEntry> getAlternatives() {
        if (alternatives == null) {
            return Collections.emptyList();
        }
        return alternatives;
    }

    SliceEntry getAlternative(ClassEntry ce) {
        if (merge(ce)) {
            return this;
        }
        if (alternatives == null) {
            return null;
        }
        for (SliceEntry alt : alternatives) {
            if (alt.merge(ce)) {
                return alt;
            }
        }
        return null;
    }

    private boolean merge(ClassEntry ce) {
        if (classEntry == ce) {
            return true;
        }
        if (ce.getPackage().getRootFile().equals(getRootFile())) {
            assert classEntry == null : "classEntry for " + ce.getClassname() + " has already been set";
            setAlternatives(ce);
            return true;
        }
        return false;
    }

    private void setAlternatives(ClassEntry ce) {
        if (ce.getAlternatives() != null) {
            assert alternatives == null : "alternatives hav already been set for " + ce.getClassname();
            alternatives = new ArrayList<SliceEntry>(ce.getAlternatives().size());
            for (ClassEntry alt : ce.getAlternatives()) {
                if (alt != ce) {
                    addAlternative(new SliceEntry(context, alt));
                }
            }
        }
    }

    void addAlternative(SliceEntry alt) {
        assert alt.alternatives == null : "alt.alternatives != null";
        if (alternatives == null) {
            alternatives = new LinkedList<SliceEntry>();
            alternatives.add(this);
        }
        assert !alternatives.contains(alt) : "alternatives.contains(alt)";
        alternatives.add(alt);
        alt.alternatives = alternatives;
    }

    public String getClassname() {
        return classname;
    }

    public URI getURI() {
        if (uri != null) {
            return uri;
        }
        if (classEntry != null) {
            uri = classEntry.getURI();
            return uri;
        }
        // either there is a classEntry or a clazz or it's unknown
        if (clazz != null) {
            uri = getURI(clazz);
        } else {
            String unknown = "unknown:" + classname;
            try {
                uri = new URI(unknown);
            } catch (URISyntaxException ex) {
                throw new IllegalStateException("Cannot convert '" + unknown + "' to URI", ex);
            }
        }
        assert uri != null : "URI has not been determined";
        return uri;
    }

    private URI getURI(Class<?> clazz) {
        URL url = clazz.getResource(clazz.getSimpleName() + ".class");
        assert url != null : "Cannot find resource for " + clazz;
        try {
            return url.toURI();
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Cannot convert '" + url + "' to URI", ex);
        }
    }
}
