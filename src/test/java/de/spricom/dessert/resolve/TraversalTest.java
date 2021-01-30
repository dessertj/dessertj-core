package de.spricom.dessert.resolve;

import de.spricom.dessert.matching.NamePattern;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class TraversalTest implements ClassVisitor {
    private final ClassResolver resolver = new ClassResolver();
    private final FakeRoot root = new FakeRoot(new File("/fakeroot"));

    private List<String> visited = new ArrayList<String>();

    @Before
    public void init() throws IOException {
        resolver.addRoot(root);
        root.add("de.spricom.aaa.Foo");
        root.add("de.spricom.aaa.Bar");
        root.add("de.spricom.bbb.Baz");
        root.add("Root");
        root.add("com.Level1");
    }

    @Test
    public void testMatchAll() {
        traverse("..*");
        assertThat(visited).hasSize(5);
    }

    @Test
    public void testResolver() {
        resolver.traverse(NamePattern.of("..*"), this);
        assertThat(visited).hasSize(5);
    }

    @Test
    public void testOneClass() {
        traverse("de.spricom.aaa.Foo");
        assertThat(visited).containsOnly("de.spricom.aaa.Foo");
    }


    @Test
    public void testRoot() {
        traverse("*");
        assertThat(visited).containsOnly("Root");
    }

    @Test
    public void testSinglePackage() {
        traverse("com.*");
        assertThat(visited).containsOnly("com.Level1");
    }

    @Test
    public void testPackageAndName() {
        traverse("..spricom.*.B*");
        assertThat(visited).containsOnly("de.spricom.aaa.Bar", "de.spricom.bbb.Baz");
    }

    @Test
    public void testName() {
        traverse("..*oo*");
        assertThat(visited).containsOnly("Root", "de.spricom.aaa.Foo");
    }

    private void traverse(String pattern) {
        root.traverse(NamePattern.of(pattern), this);
    }

    @Override
    public void visit(ClassEntry ce) {
        visited.add(ce.getClassname());
    }
}
