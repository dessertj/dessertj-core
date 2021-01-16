package de.spricom.dessert.slicing;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.resolve.ClassEntry;
import de.spricom.dessert.util.ClassUtil;
import de.spricom.dessert.util.Predicate;
import de.spricom.dessert.util.SetHelper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;

/**
 * A special {@link Slice} that represents a single .class file.
 */
public final class Clazz extends AbstractSlice implements Comparable<Clazz> {
    private static final Logger log = Logger.getLogger(Clazz.class.getName());
    public static final Clazz UNDEFINED = new Clazz();

    private final SliceContext context;
    private final String className;
    private final ClassFile classFile;
    private final ClassEntry classEntry;
    private Class<?> classImpl;
    private URI uri;

    private Clazz superclass;
    private List<Clazz> implementedInterfaces;
    private Set<Clazz> dependencies;
    private List<Clazz> alternatives;

    private Clazz() {
        context = null;
        className = "undefined";
        classFile = null;
        classEntry = null;
        superclass = this;
        implementedInterfaces = Collections.emptyList();
        dependencies = Collections.emptySet();
        alternatives = Collections.emptyList();
    }

    Clazz(SliceContext context, ClassEntry classEntry) {
        assert context != null : "context == null";
        assert classEntry != null : "classEntry == null";
        this.context = context;
        this.classFile = classEntry.getClassfile();
        this.className = classFile.getThisClass();
        this.classEntry = classEntry;
        if (classEntry.getAlternatives() != null) {
            for (ClassEntry alternative : classEntry.getAlternatives()) {
                if (classEntry != alternative) {
                    new Clazz(this, alternative);
                }
            }
        }
    }

    Clazz(Clazz alternative, ClassEntry classEntry) {
        assert alternative != null : "alternative == null";
        assert classEntry != null : "classEntry == null";
        this.context = alternative.context;
        this.classEntry = classEntry;
        this.classFile = classEntry.getClassfile();
        this.className = classFile.getThisClass();
        if (alternative.alternatives == null) {
            alternative.alternatives = new LinkedList<Clazz>();
            alternative.alternatives.add(alternative);
        }
        this.alternatives = alternative.alternatives;
        this.alternatives.add(this);
    }

    Clazz(SliceContext context, Class<?> classImpl) throws IOException {
        assert context != null : "context == null";
        assert classImpl != null : "clazz == null";
        this.context = context;
        this.classImpl = classImpl;
        this.classEntry = null;
        this.classFile = new ClassFile(classImpl);
        this.className = classFile.getThisClass();
    }

    Clazz(SliceContext context, String className) {
        assert context != null : "context == null";
        assert className != null : "className == null";
        this.context = context;
        this.classEntry = null;
        this.classFile = null;
        this.className = className;
        superclass = UNDEFINED;
        implementedInterfaces = Collections.emptyList();
        dependencies = Collections.emptySet();
        alternatives = Collections.emptyList();
    }

    public File getRootFile() {
        if (classEntry != null) {
            return classEntry.getPackage().getRootFile();
        } else if (classImpl != null) {
            return ClassUtil.getRootFile(classImpl);
        } else {
            return null;
        }
    }

    public String getPackageName() {
        if (classEntry != null) {
            return classEntry.getPackage().getPackageName();
        } else if (classImpl != null) {
            return classImpl.getPackage().getName();
        } else {
            int index = className.lastIndexOf('.');
            if (index == -1) {
                return "";
            }
            return className.substring(0, index);
        }
    }

    @Override
    public Slice combine(final Slice other) {
        if (other instanceof ConcreteSlice) {
            Set<Clazz> union = SetHelper.union(Collections.singleton(this), other.getSliceEntries());
            ConcreteSlice slice = new ConcreteSlice(union);
            return slice;
        }
        Predicate<Clazz> combined = new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz clazz) {
                return this.equals(clazz) || other.contains(clazz);
            }
        };
        return new DerivedSlice(combined);
    }

    @Override
    public Slice slice(Predicate<Clazz> predicate) {
        return predicate.test(this) ? this : Slices.EMPTY_SLICE;
    }

    @Override
    public boolean contains(Clazz clazz) {
        return equals(clazz);
    }

    @Override
    public boolean canResolveSliceEntries() {
        return true;
    }

    @Override
    public Set<Clazz> getSliceEntries() {
        return Collections.singleton(this);
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
        Clazz other = (Clazz) obj;
        if (!className.equals(other.className)) {
            return false;
        }
        return getURI().equals(other.getURI());
    }

    @Override
    public int compareTo(Clazz o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return "clazz " + className;
    }

    public boolean isUnknown() {
        return classFile == null;
    }

    public Class<?> getClassImpl() {
        if (classImpl == null && !isUnknown()) {
            try {
                classImpl = Class.forName(className);
                if (!getURI().equals(ClassUtil.getURI(classImpl))) {
                    // TODO: Use specialized classloader to prevent this
                    log.warning("Loaded class " + ClassUtil.getURI(classImpl) + " for entry " + getURI() + "!");
                }
            } catch (ClassNotFoundException ex) {
                throw new ResolveException("Unable to load " + className, ex);
            }
        }
        return classImpl;
    }

    public Clazz getSuperclass() {
        if (superclass == null && classFile != null) {
            superclass = context.asClazz(classFile.getSuperClass());
        }
        return superclass;
    }

    public List<Clazz> getImplementedInterfaces() {
        if (implementedInterfaces == null && classFile != null) {
            implementedInterfaces = new ArrayList<Clazz>(classFile.getInterfaces().length);
            for (String in : classEntry.getClassfile().getInterfaces()) {
                implementedInterfaces.add(context.asClazz(in));
            }
        }
        return implementedInterfaces;
    }

    public Set<Clazz> getDependencies() {
        if (dependencies == null && classFile != null) {
            dependencies = new HashSet<Clazz>(classFile.getDependentClasses().size());
            for (String cn : classFile.getDependentClasses()) {
                dependencies.add(context.asClazz(cn));
            }
        }
        return dependencies;
    }

    public List<Clazz> getAlternatives() {
        if (alternatives == null) {
            return Collections.singletonList(this);
        }
        return alternatives;
    }

    Clazz getAlternative(ClassEntry ce) {
        if (matches(ce)) {
            return this;
        }
        if (alternatives != null) {
            for (Clazz alt : alternatives) {
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

    public String getName() {
        return className;
    }

    public String getSimpleName() {
        int dollarIndex = className.lastIndexOf('$');
        if (dollarIndex > 0) {
            String name = className.substring(dollarIndex + 1);
            if (name.matches("\\d+")) {
                return "";
            }
            return name;
        }
        int dotIndex = className.lastIndexOf('.');
        return className.substring(dotIndex + 1);
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
        // either there is a classEntry or a classImpl or it's unknown
        if (classImpl != null) {
            uri = ClassUtil.getURI(classImpl);
        } else {
            String unknown = "dessert:unknown:" + className;
            try {
                uri = new URI(unknown);
            } catch (URISyntaxException ex) {
                throw new IllegalStateException("Cannot convert '" + unknown + "' to URI", ex);
            }
        }
        assert uri != null : "URI has not been determined";
        return uri;
    }

}
