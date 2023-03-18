package org.dessertj.resolve;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2023 Hans Jörg Heßmann
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

import org.dessertj.util.ClassUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;

public class UrlTest {

    @Test
    public void testFileUrl() {
        URL url = getClass().getResource(getClass().getSimpleName() + ".class");
        System.out.println(url);
        System.out.println(url.getProtocol());
        File dir = new File(url.getFile());
        System.out.println(dir.getAbsolutePath());
        File root = getRootFile(this.getClass());
        assertThat(root).isDirectory()
                .isEqualTo(dir.getParentFile().getParentFile().getParentFile().getParentFile());
    }

    @Test
    public void testJdkUrl() throws URISyntaxException {
        URL url = getClass().getResource("/java/io/IOException.class");
        dump(url);
        if (!"jrt".equals(url.getProtocol())) {
            // Cannot determine rt.jar for Java 9 modules.
            File root = getRootFile(IOException.class);
            assertThat(root).isFile();
            assertThat(root.getName()).isEqualTo("rt.jar");
        }
    }

    @Test
    public void testJarUrl() throws URISyntaxException {
        URL url = getClass().getResource("/org/junit/Test.class");
        dump(url);
        File root = getRootFile(Test.class);
        assertThat(root).isFile();
        assertThat(root.getName()).isEqualTo("junit-4.13.1.jar");
    }

    private void dump(URL url) throws URISyntaxException {
        System.out.println(url);
        System.out.printf("protocol: %s, file: %s, URI.path: %s%n", url.getProtocol(), url.getFile(), url.toURI().getPath());
    }

    private File getRootFile(Class<?> clazz) {
        return ClassUtils.getRootFile(clazz);
    }
}
