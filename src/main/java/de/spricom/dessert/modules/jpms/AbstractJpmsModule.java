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
import de.spricom.dessert.slicing.AbstractRootSlice;
import de.spricom.dessert.slicing.Slice;

import java.util.Collections;
import java.util.List;

public abstract class AbstractJpmsModule extends AbstractModule {

    private final ModuleAttribute moduleAttribute;
    private final AbstractRootSlice classPath;

    AbstractJpmsModule(AbstractRootSlice classPath, ClassFile moduleClassFile) {
        this.classPath = classPath;
        this.moduleAttribute = getModuleAttribute(moduleClassFile);
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

    @Override
    public Slice getUnqualifiedExports() {
        return null;
    }

    public Slice getExportsTo(String moduleName) {
        return null;
    }

    public List<JpmsModuleRequirement> requirements() {
        return Collections.emptyList();
    }
}
