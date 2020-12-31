package de.spricom.dessert.resolve;

import de.spricom.dessert.resolve.ClassEntry;
import de.spricom.dessert.resolve.ClassResolver;
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
