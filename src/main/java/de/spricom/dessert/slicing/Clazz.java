package de.spricom.dessert.slicing;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2022 Hans Jörg Heßmann
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.resolve.ClassEntry;
import de.spricom.dessert.resolve.ResolveException;
import de.spricom.dessert.util.ClassUtils;
import de.spricom.dessert.util.Predicate;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;

/**
 * A special {@link Slice} that represents a single .class file.
 */
public final class Clazz extends AbstractSlice implements Comparable<Clazz>, Concrete {
    private static final Logger log = Logger.getLogger(Clazz.class.getName());
    public static final Clazz UNDEFINED = new Clazz();

    private final Classpath classpath;
    private final String className;
    private final ClassFile classFile;
    private final ClassEntry classEntry;
    private Class<?> classImpl;
    private URI uri;

    private Clazz superclass;
    private List<Clazz> implementedInterfaces;
    private ConcreteSlice dependencies;
    private List<Clazz> alternatives;
    private Clazz host;
    private Slice nest;

    private Clazz() {
        classpath = null;
        className = "undefined";
        classFile = null;
        classEntry = null;
        superclass = this;
        implementedInterfaces = Collections.emptyList();
        dependencies = ConcreteSlice.EMPTY_SLICE;
        alternatives = Collections.emptyList();
    }

    Clazz(Classpath classpath, ClassEntry classEntry) {
        assert classpath != null : "context == null";
        assert classEntry != null : "classEntry == null";
        this.classpath = classpath;
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
        this.classpath = alternative.classpath;
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

    Clazz(Classpath classpath, Class<?> classImpl) throws IOException {
        assert classpath != null : "context == null";
        assert classImpl != null : "clazz == null";
        this.classpath = classpath;
        this.classImpl = classImpl;
        this.classEntry = null;
        this.classFile = new ClassFile(classImpl);
        this.className = classFile.getThisClass();
    }

    Clazz(Classpath classpath, String className) {
        assert classpath != null : "context == null";
        assert className != null : "className == null";
        this.classpath = classpath;
        this.classEntry = null;
        this.classFile = null;
        this.className = className;
        superclass = UNDEFINED;
        implementedInterfaces = Collections.emptyList();
        dependencies = ConcreteSlice.EMPTY_SLICE;
        alternatives = Collections.emptyList();
    }

    /**
     * @return the classes directory or jar-archive containing this class
     * @deprecated use getRoot().getRootFile() instead
     */
    @Deprecated
    public File getRootFile() {
        Root root = getRoot();
        return root == null ? null : root.getRootFile();
    }

    /**
     * @return the {@link Root} this Clazz belongs to
     */
    public Root getRoot() {
        if (classEntry != null) {
            return classpath.rootOf(classEntry.getPackage().getRoot());
        } else if (classImpl != null) {
            return classpath.rootOf(classImpl);
        } else {
            return null;
        }
    }

    public ClassFile getClassFile() {
        return classFile;
    }

    public String getName() {
        return className;
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

    public String getSimpleName() {
        if (classFile != null) {
            return classFile.getSimpleName();
        } else if (classImpl != null) {
            return classImpl.getSimpleName();
        } else {
            int dollarIndex = className.lastIndexOf('$');
            if (dollarIndex > 0) {
                String name = className.substring(dollarIndex + 1);
                if (name.matches("\\d+")) {
                    return "";
                }
                return name;
            }
            return getShortName();
        }
    }

    /**
     * @return the classname without package prefix
     */
    public String getShortName() {
        if (classEntry != null) {
            return classEntry.getShortName();
        }
        String packageName = getPackageName();
        if (packageName.isEmpty()) {
            return className;
        }
        return className.substring(packageName.length() + 1);
    }

    public URI getURI() {
        if (uri != null) {
            return uri;
        }
        if (classEntry != null) {
            uri = classEntry.getURI();
            return uri;
        }
        // either there is a classEntry or a classImpl, or it's unknown
        if (classImpl != null) {
            uri = ClassUtils.getURI(classImpl);
        } else {
            String unknown = "dessert:unknown:" + className;
            try {
                uri = new URI(unknown);
            } catch (URISyntaxException ex) {
                throw new IllegalStateException("Cannot convert '" + unknown + "' to URI", ex);
            }
        }
        return uri;
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
    public Set<Clazz> getClazzes() {
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
                if (!getURI().equals(ClassUtils.getURI(classImpl))) {
                    // TODO: Use specialized classloader to prevent this
                    log.warning("Loaded class " + ClassUtils.getURI(classImpl) + " for entry " + getURI() + "!");
                }
            } catch (ClassNotFoundException ex) {
                throw new ResolveException("Unable to load " + className, ex);
            }
        }
        return classImpl;
    }

    public Clazz getSuperclass() {
        if (superclass == null && classFile != null) {
            superclass = classpath.asClazz(classFile.getSuperClass());
        }
        return superclass;
    }

    public List<Clazz> getImplementedInterfaces() {
        if (implementedInterfaces == null && classFile != null) {
            implementedInterfaces = new ArrayList<Clazz>(classFile.getInterfaces().length);
            for (String in : classEntry.getClassfile().getInterfaces()) {
                implementedInterfaces.add(classpath.asClazz(in));
            }
        }
        return implementedInterfaces;
    }

    public Clazz getHost() {
        if (host == null) {
            if (classFile != null) {
                String nestHostName = classFile.getNestHost();
                if (nestHostName == null || nestHostName.equals(className)) {
                    host = this;
                } else {
                    host = classpath.asClazz(nestHostName);
                }
            } else if (classImpl != null) {
                Class<?> enclosingClass = classImpl;
                while (enclosingClass.getEnclosingClass() != null) {
                    enclosingClass = enclosingClass.getEnclosingClass();
                }
                host = classpath.asClazz(enclosingClass);
            } else if (className.indexOf('$') >= 0) {
                String hostClassname = className.substring(0, className.indexOf('$'));
                host = classpath.asClazz(hostClassname);
            } else {
                host = this;
            }
        }
        return host;
    }

    public Slice getNest() {
        if (nest == null) {
            ClassFile hostClassFile = getHost().classFile;
            if (hostClassFile == null) {
                nest = this;
            } else {
                List<String> nestMembers = hostClassFile.getNestMembers();
                if (nestMembers.isEmpty()) {
                    nest = this;
                } else {
                    Set<Clazz> nestClazzes = new TreeSet<Clazz>();
                    nestClazzes.add(getHost());
                    for (String nestMember : nestMembers) {
                        Clazz nestClazz = classpath.asClazz(nestMember);
                        nestClazzes.add(nestClazz);
                        if (hostClassFile.getMajorVersion() < 55) {
                            addNextMembersRecursive(nestClazzes, nestClazz);
                        }
                    }
                    nest = new ConcreteSlice(nestClazzes);
                }
            }
        }
        return nest;
    }

    private void addNextMembersRecursive(Set<Clazz> nestClazzes, Clazz outerClazz) {
        if (outerClazz.classFile == null) {
            return;
        }
        List<String> nestMembers = outerClazz.classFile.getNestMembers();
        if (nestMembers.isEmpty()) {
            return;
        }
        for (String nestMember : nestMembers) {
            Clazz nestedClazz = classpath.asClazz(nestMember);
            if (nestClazzes.add(nestedClazz)) {
                addNextMembersRecursive(nestClazzes, nestedClazz);
            }
        }
    }

    public ConcreteSlice getDependencies() {
        if (dependencies == null && classFile != null) {
            Set<Clazz> deps = new HashSet<Clazz>(classFile.getDependentClasses().size());
            for (String cn : classFile.getDependentClasses()) {
                deps.add(classpath.asClazz(cn));
            }
            dependencies = new ConcreteSlice(deps);
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

    /**
     * The min-version is available for .class files located in the META-INF directory of
     * a multi-release JAR.
     *
     * @return the minimum java version required for this class or null if not specified
     * @deprecated use rather {@link #getVersion()}
     */
    @Deprecated
    public String getMinVersion() {
        String uri = getURI().toString();
        int i = uri.toUpperCase().indexOf("/META-INF/VERSIONS/");
        int l = className.length() + "/.class".length();
        if (i > 0 && i + l < uri.length()) {
            return uri.substring(i + "/META-INF/VERSIONS/".length(), uri.length() - l);
        }
        return null;
    }

    /**
     * Returns the major version number of a Java platform release, if the .class file is located
     * in the <i>META-INF/versions/N</i> directory of a Multi-release JAR file, null otherwise.
     *
     * @return the major version number or null
     */
    public Integer getVersion() {
        return classEntry == null ? null : classEntry.getVersion();
    }
}
