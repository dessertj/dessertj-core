package de.spricom.dessert.resolve;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;

public class DirectoryRootTest {
    private DirectoryRoot directoryRoot;

    @Before
    public void init() throws IOException {
        ClassResolver resolver = ClassResolver.ofClassPath();
        ClassEntry myEntry = resolver.getClassEntry(this.getClass().getName());
        ClassRoot root = myEntry.getPackage().getRoot();
        assertThat(root).isInstanceOf(DirectoryRoot.class);
        directoryRoot = (DirectoryRoot) root;
        System.out.println("Using " + directoryRoot.getRootFile().getAbsolutePath());
    }

    @Test
    public void testGetResource() {
        URL expectedUrl1 = this.getClass().getResource(this.getClass().getSimpleName() + ".class");
        assertThat(expectedUrl1.getProtocol()).isEqualTo("file");
        URL expectedUrl = expectedUrl1;
        String path = getPath();

        URL actualUrl = directoryRoot.getResource(path);
        URL absoluteUrl = directoryRoot.getResource("/" + path);

        assertThat(actualUrl).isEqualTo(expectedUrl);
        assertThat(absoluteUrl).isEqualTo(expectedUrl);
    }

    private String getPath() {
        return this.getClass().getName().replace('.', '/') + ".class";
    }

    @Test
    public void testGetResourceAsStream() throws IOException {
        byte[] expectedBytes = IOUtils.readAll(
                this.getClass().getResourceAsStream(this.getClass().getSimpleName() + ".class"));
        String path = getPath();

        byte[] actualBytes = IOUtils.readAll(directoryRoot.getResourceAsStream(path));

        assertThat(actualBytes).isEqualTo(expectedBytes);
    }

    @Test
    public void testNonExistingResource() {
        assertThat(directoryRoot.getResource("does/not/exist.xy")).isNull();
        assertThat(directoryRoot.getResourceAsStream("does/not/exist.xy")).isNull();
    }

    @Test
    public void testNonExistingManifest() throws IOException {
        assertThat(directoryRoot.getManifest()).isNull();
    }

}
