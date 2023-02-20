package de.spricom.dessert.resolve;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
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

import de.spricom.dessert.matching.NamePattern;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class TraversalTest implements ClassVisitor {
    private final ClassResolver resolver = new ClassResolver();
    private final FakeRoot root = new FakeRoot(new File("/fake-root"));

    private final List<String> visited = new ArrayList<String>();

    @Before
    public void init() throws IOException {
        resolver.addRoot(root);
        root.add("de.spricom.aaa.Foo");
        root.add("de.spricom.aaa.Bar");
        root.add("de.spricom.bbb.Baz");
        root.add("Root");
        root.add("com.Level1");
    }

    @Test
    public void testMatchAll() {
        traverse("..*");
        assertThat(visited).hasSize(5);
    }

    @Test
    public void testResolver() {
        resolver.traverse(NamePattern.of("..*"), this);
        assertThat(visited).hasSize(5);
    }

    @Test
    public void testOneClass() {
        traverse("de.spricom.aaa.Foo");
        assertThat(visited).containsOnly("de.spricom.aaa.Foo");
    }


    @Test
    public void testRoot() {
        traverse("*");
        assertThat(visited).containsOnly("Root");
    }

    @Test
    public void testSinglePackage() {
        traverse("com.*");
        assertThat(visited).containsOnly("com.Level1");
    }

    @Test
    public void testPackageAndName() {
        traverse("..spricom.*.B*");
        assertThat(visited).containsOnly("de.spricom.aaa.Bar", "de.spricom.bbb.Baz");
    }

    @Test
    public void testName() {
        traverse("..*oo*");
        assertThat(visited).containsOnly("Root", "de.spricom.aaa.Foo");
    }

    private void traverse(String pattern) {
        root.traverse(NamePattern.of(pattern), this);
    }

    @Override
    public void visit(ClassEntry ce) {
        visited.add(ce.getClassname());
    }
}
