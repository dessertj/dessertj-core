package org.dessertj.slicing;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2025 Hans Jörg Heßmann
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

import org.dessertj.classfile.ClassFile;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DependencyClosureTest {

    @Test
    public void testClassFileClosure() {
        Classpath cp = new Classpath();
        Slice dessertj = cp.rootOf(Classpath.class);
        Slice classFile = cp.sliceOf(ClassFile.class);

        Slice closure = classFile.dependencyClosure(dessertj);

        Slice classFilePackageTree = cp.packageTreeOf(ClassFile.class)
                .slice(dessertj)
                .minus("..package-info");
        assertThat(closure.getClazzes()).isEqualTo(classFilePackageTree.getClazzes());
    }
}
