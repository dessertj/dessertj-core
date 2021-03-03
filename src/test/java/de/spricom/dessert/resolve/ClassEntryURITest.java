package de.spricom.dessert.resolve;

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

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;

public class ClassEntryURITest {
    private ClassResolver resolver;

    @Before
    public void init() throws IOException {
        resolver = ClassResolver.ofClassPath();
    }

    @Test
    public void testDirURI() throws IOException {
        check(ClassResolver.class);
    }

    @Test
    public void testJarURI() throws IOException {
        check(Assertions.class);
    }

    private void check(Class<?> clazz) {
        ClassEntry entry = resolver.getClassEntry(clazz.getName());
        String uri = entry.getURI().toString();
        System.out.println(uri);
        assertThat(uri).isEqualTo(getUrl(clazz).toString());
    }

    private URL getUrl(Class<?> clazz) {
        return clazz.getResource(clazz.getSimpleName() + ".class");
    }
}
