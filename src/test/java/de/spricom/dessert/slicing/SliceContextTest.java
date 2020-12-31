package de.spricom.dessert.slicing;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.classfile.constpool.ConstantPool;
import de.spricom.dessert.slicing.ConcreteSlice;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.SliceContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

public class SliceContextTest {
    private static SliceContext sc;

    @BeforeClass
    public static void init() throws IOException {
        sc = new SliceContext();
    }

    @Test
    public void testSliceOfClasses() {
        ConcreteSlice slice = sc.sliceOf(Slice.class, ClassFile.class, File.class);
        assertThat(slice.getSliceEntries()).hasSize(3);
    }

    @Test
    public void testPackageTreeForSinglePackage() {
        Slice slice = sc.packageTreeOf(ConstantPool.class);
        assertThat(slice.getSliceEntries()).hasSize(19);
    }

    @Test
    public void testPackageTreeForSubpackages() {
        Slice slice = sc.packageTreeOf(ClassFile.class);
        assertThat(slice.getSliceEntries()).hasSize(65);
    }
}
