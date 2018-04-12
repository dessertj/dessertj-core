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
    private final String className;
    private final ClassFile classFile;
    private final ClassEntry classEntry;
    private Class<?> clazz;
    private URI uri;

    private SliceEntry superclass;
    private List<SliceEntry> implementedInterfaces;
    private Set<SliceEntry> usedClasses;
    private List<SliceEntry> alternatives;

    private SliceEntry() {
        context = null;
        className = "undefined";
        classFile = null;
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
        this.classFile = classEntry.getClassfile();
        this.className = classFile.getThisClass();
        this.classEntry = classEntry;
        if (classEntry.getAlternatives() != null) {
            for (ClassEntry alternative : classEntry.getAlternatives()) {
                if (classEntry != alternative) {
                    new SliceEntry(this, alternative);
                }
            }
        }
    }

    SliceEntry(SliceEntry alternative, ClassEntry classEntry) {
        assert alternative != null : "alternative == null";
        assert classEntry != null : "classEntry == null";
        this.context = alternative.context;
        this.classEntry = classEntry;
        this.classFile = classEntry.getClassfile();
        this.className = classFile.getThisClass();
        if (alternative.alternatives == null) {
            alternative.alternatives = new LinkedList<SliceEntry>();
            alternative.alternatives.add(alternative);
        }
        this.alternatives = alternative.alternatives;
        this.alternatives.add(this);
    }

    SliceEntry(SliceContext context, Class<?> clazz) throws IOException {
        assert context != null : "context == null";
        assert clazz != null : "clazz == null";
        this.context = context;
        this.clazz = clazz;
        this.classEntry = null;
        this.classFile = new ClassFile(clazz);
        this.className = classFile.getThisClass();
    }

    SliceEntry(SliceContext context, String className) {
        assert context != null : "context == null";
        assert className != null : "className == null";
        this.context = context;
        this.classEntry = null;
        this.classFile = null;
        this.className = className;
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
            int index = className.lastIndexOf('.');
            if (index == -1) {
                return "";
            }
            return className.substring(0, index);
        }
    }

    @Override
    public int hashCode() {
        return className.hashCode();
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
        if (!className.equals(other.className)) {
            return false;
        }
        return getURI().equals(other.getURI());
    }

    @Override
    public int compareTo(SliceEntry o) {
        return getClassName().compareTo(o.getClassName());
    }

    @Override
    public String toString() {
        return className;
    }

    public boolean isUnknown() {
        return classFile == null;
    }

    public Class<?> getClazz() {
        if (clazz == null && !isUnknown()) {
            try {
                clazz = Class.forName(className);
                if (!getURI().equals(getURI(clazz))) {
                    // TODO: Use specialized classloader to prevent this
                    log.warning("Loaded class " + getURI(clazz) + " for entry " + getURI() + "!");
                }
            } catch (ClassNotFoundException ex) {
                throw new ResolveException("Unable to load " + className, ex);
            }
        }
        return clazz;
    }

    public SliceEntry getSuperclass() {
        if (superclass == null && classFile != null) {
            superclass = context.getSliceEntry(classFile.getSuperClass());
        }
        return superclass;
    }

    public List<SliceEntry> getImplementedInterfaces() {
        if (implementedInterfaces == null && classFile != null) {
            implementedInterfaces = new ArrayList<SliceEntry>(classFile.getInterfaces().length);
            for (String in : classEntry.getClassfile().getInterfaces()) {
                implementedInterfaces.add(context.getSliceEntry(in));
            }
        }
        return implementedInterfaces;
    }

    public Set<SliceEntry> getUsedClasses() {
        if (usedClasses == null && classFile != null) {
            usedClasses = new HashSet<SliceEntry>(classFile.getDependentClasses().size());
            for (String cn : classFile.getDependentClasses()) {
                usedClasses.add(context.getSliceEntry(cn));
            }
        }
        return usedClasses;
    }

    public List<SliceEntry> getAlternatives() {
        if (alternatives == null) {
            return Collections.singletonList(this);
        }
        return alternatives;
    }

    SliceEntry getAlternative(ClassEntry ce) {
        if (matches(ce)) {
            return this;
        }
        if (alternatives != null) {
            for (SliceEntry alt : alternatives) {
                if (alt.matches(ce)) {
                    return alt;
                }
            }
        }
        return null;
    }

    private boolean matches(ClassEntry ce) {
        if (classEntry != null) {
            return classEntry == ce;
        }
        return getURI().equals(ce.getURI());
    }

    public String getClassName() {
        return className;
    }

    public ClassFile getClassFile() {
        return classFile;
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
            String unknown = "unknown:" + className;
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
