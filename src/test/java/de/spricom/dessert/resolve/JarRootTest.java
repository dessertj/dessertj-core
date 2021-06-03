package de.spricom.dessert.resolve;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.Manifest;

import static org.fest.assertions.Assertions.assertThat;

public class JarRootTest {
    private JarRoot jarRoot;

    @Before
    public void init() throws IOException {
        ClassResolver resolver = ClassResolver.ofClassPath();
        for (File jar : resolver.getRootJars()) {
            if (jar.getName().startsWith("junit-4.")) {
                jarRoot = new JarRoot(jar);
                break;
            }
        }
        assertThat(jarRoot).as("No junit-4.* found on java.class.path").isNotNull();
        System.out.println("Using " + jarRoot.getRootFile().getAbsolutePath());
    }

    @Test
    public void testGetTopLevelResource() {
        URL expectedUrl = Test.class.getResource("/LICENSE-junit.txt");
        URL actualUrl = jarRoot.getResource("LICENSE-junit.txt");
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    public void testGetNestedResource() {
        URL expectedUrl = Test.class.getResource("/junit/runner/logo.gif");
        URL actualUrl = jarRoot.getResource("junit/runner/logo.gif");
        URL absoluteUrl = jarRoot.getResource("/junit/runner/logo.gif");
        assertThat(actualUrl).isEqualTo(expectedUrl);
        assertThat(absoluteUrl).isEqualTo(expectedUrl);
    }

    @Test
    public void testReadResource() throws IOException {
        String text = new String(IOUtils.readAll(jarRoot.getResourceAsStream("/LICENSE-junit.txt")));
        assertThat(text).contains("Eclipse Public License");
    }

    @Test
    public void testNonExistingResource() {
        assertThat(jarRoot.getResource("does/not/exist.xy")).isNull();
        assertThat(jarRoot.getResourceAsStream("does/not/exist.xy")).isNull();
    }

    @Test
    public void testManifest() throws IOException {
        Manifest expectedManifest = new Manifest(jarRoot.getResourceAsStream("META-INF/MANIFEST.MF"));
        Manifest actualManifest = jarRoot.getManifest();
        String moduleName = actualManifest.getMainAttributes().getValue("Automatic-Module-Name");
        assertThat(actualManifest).isEqualTo(expectedManifest);
        assertThat(moduleName).isEqualTo("junit");
    }

}
