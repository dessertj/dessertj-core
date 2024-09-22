package org.dessertj.samples;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2024 Hans Jörg Heßmann
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
import org.dessertj.samples.enums.SomeEnum;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

/**
 * This test has been generated by TestGeneratorTool.
 */
public class EnumsTest {

    @Test
    public void testSomeEnum() throws IOException {
        ClassFile cf = new ClassFile(SomeEnum.class);
        Set<String> dependentClasses = cf.getDependentClasses();
        assertThat(dependentClasses).containsOnly(
                "java.lang.Class",
                "java.lang.Enum",
                "java.lang.Object",
                "java.lang.String");
    }

}
