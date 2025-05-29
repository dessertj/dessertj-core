package org.dessertj.matching;

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

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import static org.fest.assertions.Assertions.assertThat;

public class NamePatternTest {

    private void check(String pattern, String name, boolean expectedResult) {
        NamePattern namePattern = NamePattern.of(pattern);
        assertThat(namePattern.toString()).isEqualTo(pattern);
        assertThat(namePattern.matches(name))
                .as("\"" + name + "\"/" + pattern + "/")
                .isEqualTo(expectedResult);
    }

    @Test
    public void testSingle() {
        check("module-info", "module-info", true);
    }

    @Test
    public void testName() {
        check("sample.Foo", "sample.Foo", true);
        check("sample.Foo", "sample.Fool", false);
        check("sample.*", "sample.Foo", true);
        check("*.*", "sample.Foo", true);
        check("*.Foo", "sample.Foo", true);
        check("sam*.*oo", "sample.Foo", true);
        check("*a*l*.F*o", "sample.Foo", true);
        check("*.*u*", "sample.Foo", false);
        check("*", "sample.Foo", false);
        check("*", "Foo", true);
    }

    @Test
    public void testInnerTypes() {
        check("sample.Foo.Bar", "sample.Foo.Bar", true);
        check("sample.Foo$Bar", "sample.Foo$Bar", true);
        check("sample.Foo$*", "sample.Foo$Bar", true);
        check("sample.Foo*", "sample.Foo$Bar", true);
        check("sample.Foo$Bar", "sample.Foo.Bar", false);
        check("sample.Foo$Ba*z", "sample.Foo$Bar$Baz", true);
    }

    @Test
    public void testAlternatives() {
        check("sample.bar|baz.*", "sample.bar.Foo", true);
        check("sample.bar|baz.*", "sample.baz.Foo", true);
        check("sample.Foo|*", "sample.Bar", true);
        check("|.Foo", "sample.Foo", false);
        check("|*|.Foo", "sample.Foo", true);
    }

    @Test
    public void testPackage() {
        check("sample..Baz", "sample.foo.bar.Baz", true);
        check("sample..bar.Baz", "sample.foo.bar.Baz", true);
        check("sample..foo.bar.Baz", "sample.foo.bar.Baz", true);
        check("sample..foo..bar..Baz", "sample.foo.bar.Baz", true);
        check("..foo.bar.Baz", "sample.foo.bar.Baz", true);
        check("..*", "sample.foo.bar.Baz", true);
        check("..*.*", "sample.foo.bar.Baz", true);
        check("..*.*.*", "sample.foo.bar.Baz", true);
        check("..*.*.*.*", "sample.foo.bar.Baz", true);
        check("..*.*.*.*.*", "sample.foo.bar.Baz", false);
        check("..*..*", "sample.foo.bar.Baz", true);
        check("*..*..*", "sample.foo.Baz", true);
        check("*..*..*", "sample.Baz", false);
    }

    @Test
    public void testClassWildcard() {
        check("..foo.bar.*", "com.country.foo.bar.baz.Blub", false);
        check("..foo.bar.*", "com.country.foo.bar.Blub", true);
    }

    private void checkInvalid(String pattern) {
        try {
            NamePattern.of(pattern);
            Assert.fail("no exception for: " + pattern);
        } catch (IllegalArgumentException ex) {
            // ignore
        }
    }

    @Test
    public void testInvalidPatterns() {
        checkInvalid("..");
        checkInvalid("sample...Foo");
    }

    @Test
    public void testSorting() {
        List<NamePattern> patterns = new LinkedList<NamePattern>();
        patterns.add(NamePattern.of("sample.foo.bar.Baz"));
        patterns.add(NamePattern.of("sample.foo.Baz"));
        patterns.add(NamePattern.of("*a*l*.F*o"));
        patterns.add(NamePattern.of("sample.Foo$Bar$Baz"));
        patterns.add(NamePattern.of("sample.Foo*"));
        patterns.add(NamePattern.of("sample..foo..bar..Baz"));
        patterns.add(NamePattern.of("*..*..*"));
        patterns.add(NamePattern.of("sample..bar.Baz"));
        patterns.add(NamePattern.ANY_NAME);
        assertThat(new TreeSet<NamePattern>(patterns).toString()).isEqualTo(patterns.toString());
    }

    @Test
    public void testSortingDuplicates() {
        TreeSet<NamePattern> patterns = new TreeSet<NamePattern>();
        patterns.add(NamePattern.ANY_NAME);
        patterns.add(NamePattern.of("sample.Foo*"));
        patterns.add(NamePattern.ANY_NAME);
        patterns.add(NamePattern.of("sample.Foo*"));
        assertThat(patterns.toString()).isEqualTo("[sample.Foo*, ..*]");
    }

}
