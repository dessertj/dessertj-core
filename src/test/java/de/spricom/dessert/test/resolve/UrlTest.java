package de.spricom.dessert.test.resolve;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Objects;

import org.junit.Test;

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
        String filename = "/" + clazz.getName().replace('.', '/') + ".class";
        URL url = clazz.getResource(clazz.getSimpleName() + ".class");
        Objects.requireNonNull(url, "Resource for " + clazz + " not found!");
        switch (url.getProtocol()) {
        case "file":
            assert url.getFile().endsWith(filename) : url + " does not end with " + filename;
            return new File(url.getFile().substring(0, url.getFile().length() - filename.length()));
        case "jar":
            assert url.getFile().startsWith("file:") : url + " does not start with jar:file";
            assert url.getFile().endsWith(".jar!" + filename) : url + " does not end with .jar!" + filename;
            try {
                return new File(URLDecoder.decode(url.getFile().substring("file:".length(), url.getFile().length() - filename.length() - 1), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                throw new IllegalStateException("UTF-8 encoding not supported!", ex);
            }
        default:
            throw new IllegalArgumentException("Unknown protocol in " + url);
        }
    }
}
