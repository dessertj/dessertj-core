package de.spricom.dessert.test.slicing;

import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.SliceContext;
import de.spricom.dessert.slicing.SliceEntry;
import de.spricom.dessert.test.resolve.FakeClassEntry;
import de.spricom.dessert.test.resolve.FakeRoot;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class SliceEntryTest {

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
        Set<SliceEntry> entries = slice.getSliceEntries();
        assertThat(entries).hasSize(1);
        SliceEntry entry = entries.iterator().next();
        assertThat(entry.getAlternatives()).hasSize(2);
    }
}
