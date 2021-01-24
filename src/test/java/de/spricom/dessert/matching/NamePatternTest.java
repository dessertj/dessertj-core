package de.spricom.dessert.matching;

import org.junit.Assert;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class NamePatternTest {

    private void check(String pattern, String name, boolean expectedResult) {
        NamePattern namePattern = NamePattern.of(pattern);
        assertThat(namePattern.matches(name))
                .as("\"" + name + "\"/" + pattern + "/")
                .isEqualTo(expectedResult);
    }

    @Test
    public void testSingle() {
        check("*", "sample.Foo", false);
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
}
