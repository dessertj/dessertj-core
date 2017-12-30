package de.spricom.dessert.test.resolve;

import de.spricom.dessert.slicing.SliceEntry;
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
                .isEqualTo(dir.getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile());
    }

    @Test
    public void testJarUrl() throws URISyntaxException {
        URL url = getClass().getResource("/java/io/IOException.class");
        System.out.println(url);
        System.out.println(url.getProtocol());
        System.out.println(url.getFile());
        System.out.println(url.toURI().getPath());
        File root = getRootFile(IOException.class);
        assertThat(root).isFile();
        assertThat(root.getName()).isEqualTo("rt.jar");
    }

    private File getRootFile(Class<?> clazz) {
        return SliceEntry.getRootFile(clazz);
    }
}
