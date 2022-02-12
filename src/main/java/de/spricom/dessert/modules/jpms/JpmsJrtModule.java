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
import de.spricom.dessert.slicing.AbstractRootSlice;
import de.spricom.dessert.slicing.Slice;

public class JpmsJrtModule extends AbstractJpmsModule {

    public JpmsJrtModule(AbstractRootSlice classPath, ClassFile moduleClassFile) {
        super(classPath, moduleClassFile);
    }

    @Override
    public Slice getImplementation() {
        return null;
    }
}
