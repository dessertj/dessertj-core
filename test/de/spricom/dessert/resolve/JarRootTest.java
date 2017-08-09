package de.spricom.dessert.resolve;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

public class JarRootTest {
    private JarRoot cr;
    
    @Before
    public void init() throws URISyntaxException {
        String url = Test.class.getResource("Test.class").toExternalForm();
        int a = "jar:file:".length();
        int b = url.length() - Test.class.getName().length() - "!/.class".length();
        File root = new File(url.substring(a, b));
        assertThat(root).isFile();
        assertThat(root.getName()).endsWith(".jar");
        cr = new JarRoot(root);
    }
    
    @Test
    public void testExisting() throws IOException {
        String packagename = Test.class.getPackage().getName();
        assertThat(cr.resolve(packagename)).isTrue();
        ClassContainer cc = cr;
        for (String segment : packagename.split("\\.")) {
            cc = cc.find(segment);
            assertThat(cc).as(segment).isNotNull();
        }
        assertThat(cr.resolve("nonsense")).isTrue();
    }

    @Test
    public void testNotExisting() throws IOException {
        String packagename = this.getClass().getPackage().getName() + ".blub";
        assertThat(cr.resolve(packagename)).isTrue();
        assertThat(cr.resolve("nonsense")).isTrue();
        assertThat(cr.getFirstChild()).isNotNull();
    }

}
