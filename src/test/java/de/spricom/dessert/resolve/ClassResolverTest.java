package de.spricom.dessert.resolve;

/*-
 * #%L
 * Dessert Dependency Assertion Library
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

import de.spricom.dessert.classfile.dependency.DependencyHolder;
import de.spricom.dessert.samples.basic.Foo;
import org.fest.assertions.Condition;
import org.junit.Assume;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

import static org.fest.assertions.Assertions.assertThat;

public class ClassResolverTest {
    private static Logger log = Logger.getLogger(ClassResolverTest.class.getName());
    private static ClassResolver defaultResolver;

    @Test
    public void testClassPathWithoutJars() throws IOException {
        ClassResolver resolver = ClassResolver.ofClassPathWithoutJars();
        assertThat(resolver.getRootFiles()).isEqualTo(resolver.getRootDirs());
        // If there are separate directories for productive an test classes then we have two root dirs.
        assertThat(resolver.getRootDirs().size()).isLessThanOrEqualTo(2);
        assertThat(resolver.getRootJars()).isEmpty();

        ClassPackage cp = resolver.getPackage("de.spricom.dessert.samples.basic");
        assertThat(cp.getPackageName()).isEqualTo("de.spricom.dessert.samples.basic");
        assertThat(cp.toString()).isEqualTo("de.spricom.dessert.samples.basic");
        assertThat(cp.getSubPackages()).hasSize(0);
        assertThat(cp.getParent().getPackageName()).isEqualTo("de.spricom.dessert.samples");

        assertThat(cp.getClasses()).hasSize(9);
        ClassEntry cf = resolver.getClassEntry(Foo.class.getName());
        assertThat(cf.getClassfile().getThisClass()).isEqualTo(Foo.class.getName());
        assertThat(cf.getPackage()).isSameAs(cp);
        assertThat(cf.getAlternatives()).isNull();

        ClassPackage parent = resolver.getPackage("de.spricom");
        assertThat(parent.getSubPackages()).hasSize(1);

        assertThat(resolver.getPackage("de.spricom.notthere")).isNull();
        assertThat(resolver.getPackage("")).isNotNull().isInstanceOf(ClassRoot.class);
        assertThat(resolver.getRoot(resolver.getRootDirs().iterator().next()).getPackageName()).isEmpty();
    }

    @Test
    public void testAlternativePackages() throws IOException {
        ClassResolver resolver = ClassResolver.ofClassPath();
        Assume.assumeTrue("There are separate directories for productive an test classes",
                resolver.getRootDirs().size() == 2);

        ClassPackage cp1 = resolver.getPackage("de.spricom.dessert.classfile");
        assertThat(cp1.getAlternatives()).hasSize(2);

        String classname = Foo.class.getName();
        ClassEntry cf1 = resolver.getClassEntry(classname);
        assertThat(cf1.getClassfile().getThisClass()).isEqualTo(classname);
        assertThat(resolver.getClassEntry(cf1.getPackage().getRootFile(), classname)).isSameAs(cf1);
        assertThat(resolver.getClassEntry(
                resolver.getPackage(DependencyHolder.class.getPackage().getName()).getRootFile(),
                classname)).isNull();
    }

    @Test
    public void testClassPath() throws IOException {
        ClassResolver resolver = ClassResolver.ofClassPath();
        assertThat(resolver.getRootDirs()).isNotEmpty();
        assertThat(resolver.getRootJars()).isNotEmpty();
        assertThat(resolver.getRootDirs().size() + resolver.getRootJars().size()).isEqualTo(resolver.getRootFiles().size());
        assertThat(resolver.getRootJars()).is(new ContainsFile("junit-4.13.1.jar"));
    }

    static class ContainsFile extends Condition<Collection<?>> {
        private final String filename;

        ContainsFile(String filename) {
            super("does not contain file " + filename);
            this.filename = filename;
        }

        @Override
        public boolean matches(Collection<?> files) {
            for (Object file : files) {
                if (filename.equals(((File)file).getName())) {
                    return true;
                }
            }
            return false;
        }
    }
}
