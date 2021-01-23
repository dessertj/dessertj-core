package de.spricom.dessert.slicing;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.classfile.constpool.ConstantPool;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

public class ClasspathTest {
    private static Classpath sc;

    @BeforeClass
    public static void init() {
        sc = new Classpath();
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
