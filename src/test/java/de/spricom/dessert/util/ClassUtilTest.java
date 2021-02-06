package de.spricom.dessert.util;

import org.junit.Test;

import java.io.File;
import java.net.URI;

import static org.fest.assertions.Assertions.assertThat;

public class ClassUtilTest {

    @Test
    public void testDirectory() {
        Class<?> clazz = this.getClass();
        URI uri = ClassUtil.getURI(clazz);
        File rootFile = ClassUtil.getRootFile(clazz);

        assertThat(uri.toASCIIString())
                .startsWith("file:/")
                .endsWith("/dessert-core/target/test-classes/de/spricom/dessert/util/ClassUtilTest.class");
        assertThat(rootFile.getName()).isEqualTo("test-classes");
    }

    @Test
    public void testJar() {
        Class<?> clazz = org.fest.assertions.Assertions.class;
        URI uri = ClassUtil.getURI(clazz);
        File rootFile = ClassUtil.getRootFile(clazz);

        assertThat(uri.toASCIIString())
                .startsWith("jar:file:/")
                .endsWith(".jar!/org/fest/assertions/Assertions.class");
        assertThat(rootFile.getName()).isEqualTo("fest-assert-1.4.jar");
    }
}
