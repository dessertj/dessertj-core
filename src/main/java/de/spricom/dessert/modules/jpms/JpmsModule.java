package de.spricom.dessert.modules.jpms;

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
import de.spricom.dessert.classfile.attribute.Attributes;
import de.spricom.dessert.classfile.attribute.ModuleAttribute;
import de.spricom.dessert.modules.core.AbstractModule;
import de.spricom.dessert.slicing.Clazz;
import de.spricom.dessert.slicing.ResolveException;
import de.spricom.dessert.slicing.Root;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.util.Predicate;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public final class JpmsModule extends AbstractModule {

    private final ModuleAttribute moduleAttribute;
    private final Root root;

    public JpmsModule(Root root) {
        this.root = root;
        ClassFile moduleInfo = getModuleInfo(root);
        this.moduleAttribute = getModuleAttribute(moduleInfo);
    }

    private ClassFile getModuleInfo(Root root) {
        InputStream is = root.getResourceAsStream("module-info.class");
        if (is == null) {
            throw new ResolveException(root.getURI() + " does not contain a module-info.class.");
        }
        try {
            return new ClassFile(is);
        } catch (IOException ex) {
            throw new ResolveException("Unable to scan " + root.getURI() + "/module-info.class");
        }
    }

    private ModuleAttribute getModuleAttribute(ClassFile moduleClassFile) {
        if (!(moduleClassFile.isModule()
                && moduleClassFile.getThisClass().endsWith("module-info"))) {
            throw new IllegalArgumentException(moduleClassFile.getThisClass() + " is not a module-info.class!");
        }
        List<ModuleAttribute> moduleAttributes =
                Attributes.filter(moduleClassFile.getAttributes(), ModuleAttribute.class);
        if (moduleAttributes.size() != 1) {
            throw new IllegalArgumentException(moduleClassFile.getThisClass() + " does not contain a ModuleAttribute!");
        }
        return moduleAttributes.get(0);
    }

    @Override
    public String getName() {
        return moduleAttribute.getModuleName();
    }

    @Override
    public String getVersion() {
        return moduleAttribute.getModuleVersion();
    }

    public Root getRoot() {
        return root;
    }

    @Override
    public Root getImplementation() {
        return getRoot();
    }

    @Override
    public Slice getUnqualifiedExports() {
        return getExportsTo(null);
    }

    public Slice getExportsTo(String moduleName) {
        final Set<String> exports = new HashSet<String>(moduleAttribute.getExports().length * 2);
        for (ModuleAttribute.Export export : moduleAttribute.getExports()) {
            if (export.isUnqualified()) {
                exports.add(export.getPackageName());
            } else if (moduleName != null) {
                for (String targetModule : export.getExportsTo()) {
                    if (targetModule.equals(moduleName)) {
                        exports.add(export.getPackageName());
                    }
                }
            }
        }
        return root.slice(new ExportedPredicate(exports));
    }

    public List<JpmsModuleRequirement> requirements() {
        ModuleAttribute.Require[] requires = moduleAttribute.getRequires();
        if (requires.length == 0) {
            return Collections.emptyList();
        }
        JpmsModuleRequirement[] requirements = new JpmsModuleRequirement[requires.length];
        for (int i = 0; i < requires.length; i++) {
            requirements[i] = new JpmsModuleRequirement(requires[i]);
        }
        return Arrays.asList(requirements);
    }

    static class ExportedPredicate implements Predicate<Clazz> {
        final Set<String> exports;

        public ExportedPredicate(Set<String> exports) {
            this.exports = exports;
        }

        @Override
        public boolean test(Clazz clazz) {
            return exports.contains(clazz.getPackageName());
        }
    }
}
