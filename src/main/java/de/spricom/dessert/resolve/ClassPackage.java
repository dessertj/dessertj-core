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

import de.spricom.dessert.matching.ShortNameMatcher;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

/**
 * A ClassEntry represents a single package within one classes directory or .jar file.
 * It's either linked to the parent {@link ClassPackage} it belongs to or it is a
 * {@link ClassRoot} itself. It maintains a probably empty list of all its subpackages
 * and classes it contains directly.
 * If there is another package with the same name in some other classes directory or
 * .jar file within the {@link ClassResolver} scope it has a reference to a list of all
 * such packages.
 */
public class ClassPackage {
    private static final Logger log = Logger.getLogger(ClassPackage.class.getName());

    private final String packageName;
    private final ClassPackage parent;
    private final Map<String, ClassPackage> subPackages = new TreeMap<String, ClassPackage>();
    private final Map<String, ClassEntry> classes = new TreeMap<String, ClassEntry>();
    private List<ClassPackage> alternatives;

    protected ClassPackage() {
        packageName = "";
        parent = null;
    }

    public ClassPackage(ClassPackage parent, String packageName) {
        this.parent = parent;
        this.packageName = packageName;
        if (parent.subPackages.put(packageName, this) != null) {
            log.warning(packageName + " appears twice.");
        }
    }

    public ClassPackage getParent() {
        return parent;
    }

    public final String getPackageName() {
        return packageName;
    }

    public final String getClassName(String simpleName) {
        return packageName.isEmpty() ? simpleName : packageName + "." + simpleName;
    }

    /**
     * @return the package name without parent package prefix
     */
    public String getShortName() {
        if (parent == null) {
            return "";
        }
        String parentPackageName = getParent().getPackageName();
        if (parentPackageName.isEmpty()) {
            return this.packageName;
        }
        return this.packageName.substring(parentPackageName.length() + 1);
    }

    public ClassRoot getRoot() {
        return parent.getRoot();
    }

    @Deprecated
    public File getRootFile() {
        return getRoot().getRootFile();
    }

    @Override
    public String toString() {
        return packageName;
    }

    /**
     * @return the probably empty list of all direct nested packages of this package
     */
    public List<ClassPackage> getSubPackages() {
        return new ArrayList<ClassPackage>(subPackages.values());
    }

    public ClassPackage getSubPackage(String packageName) {
        return subPackages.get(packageName);
    }

    /**
     * @return the list of all classes directly contained in this package
     */
    public List<ClassEntry> getClasses() {
        return new ArrayList<ClassEntry>(classes.values());
    }

    public void addClass(ClassEntry ce) {
        if (classes.put(ce.getClassname(), ce) != null) {
            log.warning(ce.getURI() + " appears twice, using last one.");
        }
    }

    public ClassEntry getClass(String classname) {
        return classes.get(classname);
    }

    /**
     * Returns a list of all other packages with the same fully qualified name.
     * For the root package "" this method returns all roots ({@link ClassRoot})
     * within the {@link ClassResolver}.
     *
     * @return the other packages or null if there are none
     */
    public List<ClassPackage> getAlternatives() {
        return alternatives;
    }

    void addAlternative(ClassPackage alt) {
        assert alt.alternatives == null : "alt.alternatives != null";
        if (alternatives == null) {
            alternatives = new LinkedList<ClassPackage>();
            alternatives.add(this);
        }
        assert !alternatives.contains(alt) : "alternatives.contains(alt)";
        alternatives.add(alt);
        alt.alternatives = alternatives;
    }

    protected final void traverse(ShortNameMatcher matcher, ClassVisitor visitor) {
        if (!matcher.isMatchPossible()) {
            return;
        }
        if (matcher.isLast()) {
            traverseClasses(matcher, visitor);
        } else {
            traverseSubPackages(matcher, visitor);
            if (matcher.isWildcard()) {
                traverse(matcher.next(), visitor);
            }
        }
    }

    private void traverseClasses(ShortNameMatcher matcher, ClassVisitor visitor) {
        for (ClassEntry clazz : classes.values()) {
            if (matcher.match(clazz.getShortName()).matches()) {
                visitor.visit(clazz);
                for (ClassEntry versionedAlternative : clazz.getAlternativesWithinSamePackage()) {
                    visitor.visit(versionedAlternative);
                }
            }
        }
    }

    private void traverseSubPackages(ShortNameMatcher matcher, ClassVisitor visitor) {
        for (ClassPackage subPackage : subPackages.values()) {
            subPackage.traverse(matcher.match(subPackage.getShortName()), visitor);
        }
    }
}
