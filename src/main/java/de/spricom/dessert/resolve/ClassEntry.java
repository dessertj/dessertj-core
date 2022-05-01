package de.spricom.dessert.resolve;

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

import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A ClassEntry represents a single class file within one classes directory or .jar file.
 * It's linked to the {@link ClassPackage} it belongs to.
 * If there is another .class file with the same name in some other classes directory or
 * .jar file within the {@link ClassResolver} scope it has a reference to a list of all
 * such .class files.
 */
public abstract class ClassEntry {
    private final String classname;
    private final ClassPackage pckg;
    private List<ClassEntry> alternatives;
    private ClassFile classFile;

    protected ClassEntry(String classname, ClassPackage pckg) {
        this.classname = classname;
        this.pckg = pckg;
    }

    /**
     * @return the full qualified classname
     */
    public String getClassname() {
        return classname;
    }

    /**
     * @return the classname without package prefix
     */
    public String getShortName() {
        if (pckg.getPackageName().isEmpty()) {
            return classname;
        }
        return classname.substring(getPackage().getPackageName().length() + 1);
    }

    /**
     * Resolves the {@link ClassFile} by analyzing the byte code. This is a time-consuming operation and
     * should be called as late as possible. For .jar files the most time is spent by decompressing the
     * .class file. The result will be cached for further calls.
     *
     * @return a cached or freshly resolved instance of the {@link ClassFile}
     */
    public ClassFile getClassfile() {
        if (classFile == null) {
            classFile = resolveClassFile();
            assert classname.equals(classFile.getThisClass()) : classname + " != " + classFile.getThisClass();
        }
        return classFile;
    }

    protected abstract ClassFile resolveClassFile();

    /**
     * @return the {@link ClassPackage} this ClassEntry belongs to.
     */
    public ClassPackage getPackage() {
        return pckg;
    }

    /**
     * Returns a list of all other .class files with the same fully qualified name.
     *
     * @return the other .class files or null if there are none
     */
    public List<ClassEntry> getAlternatives() {
        return alternatives;
    }

    /**
     * Returns a list of all other .class files with the same fully qualified name with the same {@link ClassRoot}.
     * Such .class files exist within multi-release JAR files within the META-INF/versions directory.
     *
     * @return all other versions of the same .class file or an empty list if there are none
     */
    public List<ClassEntry> getAlternativesWithinSamePackage() {
        if (alternatives == null) {
            return Collections.emptyList();
        }
        List<ClassEntry> versioned = new LinkedList<ClassEntry>();
        for (ClassEntry alternative : alternatives) {
            if (alternative.getPackage() == pckg) {
                versioned.add(alternative);
            }
        }
        return versioned;
    }

    void addAlternative(ClassEntry alt) {
        assert alt.alternatives == null : "alt.alternatives != null";
        if (alternatives == null) {
            alternatives = new LinkedList<ClassEntry>();
            alternatives.add(this);
        }
        assert !alternatives.contains(alt) : "alternatives.contains(alt)";
        alternatives.add(alt);
        alt.alternatives = alternatives;
    }

    /**
     * @return an {@link URI} which identifies the .class file uniquely
     */
    public abstract URI getURI();
}
