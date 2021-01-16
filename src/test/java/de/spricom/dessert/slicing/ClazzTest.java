package de.spricom.dessert.slicing;

import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.resolve.FakeClassEntry;
import de.spricom.dessert.resolve.FakeRoot;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class ClazzTest {

    @Test
    public void testThisClass() throws MalformedURLException {
        SliceContext sc = new SliceContext();
        Slice slice = sc.sliceOf(ClazzTest.class.getName());
        Set<Clazz> entries = slice.getSliceEntries();
        assertThat(entries).hasSize(1);
        Clazz entry = entries.iterator().next();
        assertThat(entry.getAlternatives()).hasSize(1);

        assertThat(entry.getClassName()).isEqualTo(getClass().getName());
        assertThat(entry.getClassFile().getThisClass()).isEqualTo(getClass().getName());
        assertThat(entry.getClassImpl()).isSameAs(getClass());
        assertThat(entry.getPackageName()).isEqualTo(getClass().getPackage().getName());
        
        assertThat(entry.getSuperclass().getClassImpl()).isSameAs(Object.class);
        assertThat(entry.getImplementedInterfaces()).isEmpty();
        assertThat(entry.getURI().toURL()).isEqualTo(getClass().getResource(getClass().getSimpleName() + ".class"));
        assertThat(new File(entry.getURI().toURL().getPath()).getAbsolutePath()).startsWith(entry.getRootFile().getAbsolutePath());
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

        SliceContext sc = new SliceContext(resolver);
        Slice slice = sc.packageTreeOf("de.spricom.dessert");
        Set<Clazz> entries = slice.getSliceEntries();
        assertThat(entries).hasSize(2);
        Clazz entry = entries.iterator().next();
        assertThat(new HashSet<Clazz>(entry.getAlternatives())).isEqualTo(entries);

        Slice duplicates = sc.duplicates();
        assertThat(duplicates.getSliceEntries()).isEqualTo(entries);
    }
}
