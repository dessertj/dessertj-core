package de.spricom.dessert.util;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2021 Hans Jörg Heßmann
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

import org.junit.Test;

import java.io.File;
import java.net.URI;

import static org.fest.assertions.Assertions.assertThat;

public class ClassUtilsTest {

    @Test
    public void testDirectory() {
        Class<?> clazz = this.getClass();
        URI uri = ClassUtils.getURI(clazz);
        File rootFile = ClassUtils.getRootFile(clazz);

        assertThat(uri.toASCIIString())
                .startsWith("file:/")
                .endsWith("/target/test-classes/de/spricom/dessert/util/ClassUtilsTest.class");
        assertThat(rootFile.getName()).isEqualTo("test-classes");
    }

    @Test
    public void testJar() {
        Class<?> clazz = org.fest.assertions.Assertions.class;
        URI uri = ClassUtils.getURI(clazz);
        File rootFile = ClassUtils.getRootFile(clazz);

        assertThat(uri.toASCIIString())
                .startsWith("jar:file:/")
                .endsWith(".jar!/org/fest/assertions/Assertions.class");
        assertThat(rootFile.getName()).isEqualTo("fest-assert-1.4.jar");
    }
}
