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
        Slice slice = sc.sliceOf(Slice.class, ClassFile.class, File.class);
        assertThat(slice.getSliceEntries()).hasSize(3);
    }

    /**
     * Use @code{find target/classes/de/spricom/dessert/classfile/constpool -iname "*.class" | wc -l}
     * to determine the expected result.
     */
    @Test
    public void testPackageTreeForSinglePackage() {
        int expectedNumberOfClasses = 25;
        Slice slice = sc.packageTreeOf(ConstantPool.class);
        assertThat(slice.getSliceEntries()).hasSize(expectedNumberOfClasses);
    }


    /**
     * The exepected result is the sum of
     * @code{find target/classes/de/spricom/dessert/classfile -iname "*.class" | wc -l} and
     * @code{find target/test-classes/de/spricom/dessert/classfile -iname "*.class" | wc -l}.
     */
    @Test
    public void testPackageTreeForSubpackages() {
        int expectedNumberOfClasses = 46;
        int expectedNumberOfTestClasses = 14;
        Slice slice = sc.packageTreeOf(ClassFile.class);
        assertThat(slice.getSliceEntries()).hasSize(expectedNumberOfClasses + expectedNumberOfTestClasses);
    }
}
