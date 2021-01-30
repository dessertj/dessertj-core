package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.resolve.FakeClassEntry;
import de.spricom.dessert.resolve.FakeRoot;
import de.spricom.dessert.samples.basic.Outer;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class ClazzTest {

    private final Classpath sc = new Classpath();

    @Test
    public void testThisClass() throws MalformedURLException {
        Slice slice = sc.sliceOf(ClazzTest.class.getName());
        Set<Clazz> entries = slice.getClazzes();
        assertThat(entries).hasSize(1);
        Clazz entry = entries.iterator().next();
        assertThat(entry.getAlternatives()).hasSize(1);

        assertThat(entry.getName()).isEqualTo(getClass().getName());
        assertThat(entry.getSimpleName()).isEqualTo(getClass().getSimpleName());
        assertThat(entry.getClassFile().getThisClass()).isEqualTo(getClass().getName());
        assertThat(entry.getClassImpl()).isSameAs(getClass());
        assertThat(entry.getPackageName()).isEqualTo(getClass().getPackage().getName());
        
        assertThat(entry.getSuperclass().getClassImpl()).isSameAs(Object.class);
        assertThat(entry.getImplementedInterfaces()).isEmpty();
        assertThat(entry.getURI().toURL()).isEqualTo(getClass().getResource(getClass().getSimpleName() + ".class"));
        assertThat(new File(entry.getURI().toURL().getPath()).getAbsolutePath()).startsWith(entry.getRootFile().getAbsolutePath());
    }

    @Test
    public void testInnerClass() {
        checkName(Outer.InnerIfc.class);
    }

    @Test
    public void testAnonymousInnerClass() {
        Outer outer = new Outer();
        Outer.InnerIfc anonymous = outer.useAnonymous();
        checkName(anonymous.getClass());
    }

    private void checkName(Class<?> classImpl) {
        Clazz clazz = sc.sliceOf(classImpl).getClazzes().iterator().next();

        System.out.println("Checking " + clazz.getName());
        assertThat(clazz.getName()).isEqualTo(classImpl.getName());
        assertThat(clazz.getSimpleName()).isEqualTo(classImpl.getSimpleName());
    }

    @Test
    public void testCreateSliceEntryWithAlternative() throws IOException {
        ClassResolver resolver = new ClassResolver();
        FakeRoot root1 = new FakeRoot(new File("/root1"));
        resolver.addRoot(root1);
        FakeRoot root2 = new FakeRoot(new File("/root2"));
        resolver.addRoot(root2);

        String fakeClassName = FakeClassEntry.class.getName();
        root1.add(fakeClassName);
        root2.add(fakeClassName);

        Classpath sc = new Classpath(resolver);
        Slice slice = sc.packageTreeOf("de.spricom.dessert");
        Set<Clazz> entries = slice.getClazzes();
        assertThat(entries).hasSize(2);
        Clazz entry = entries.iterator().next();
        assertThat(new HashSet<Clazz>(entry.getAlternatives())).isEqualTo(entries);

        Slice duplicates = sc.duplicates();
        assertThat(duplicates.getClazzes()).isEqualTo(entries);
    }
}
