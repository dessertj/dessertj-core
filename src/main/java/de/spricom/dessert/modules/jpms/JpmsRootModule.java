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
import de.spricom.dessert.slicing.Root;

import java.io.IOException;
import java.io.InputStream;

public class JpmsRootModule extends JpmsModule {

    private final Root root;

    public JpmsRootModule(Root root) throws IOException {
        super(root, getModuleInfo(root));
        this.root = root;
    }

    private static final ClassFile getModuleInfo(Root root) throws IOException {
        InputStream is = root.getResourceAsStream("/module-info.class");
        if (is == null) {
            throw new IllegalArgumentException(root.getRootFile() + " does not contain a module-info.class.");
        }
        return new ClassFile(is);
    }

    @Override
    public Root getImplementation() {
        return null;
    }
}
