package de.spricom.dessert.test.classfile;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.test.classfile.samples.EmptyGenericWithBounds;
import de.spricom.dessert.test.classfile.samples.GenericWithoutBounds;
import de.spricom.dessert.test.classfile.samples.GenericWithoutBoundsEmpty;
import de.spricom.dessert.test.classfile.samples.SpecialArgSample;
import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

public class SamplesDependenciesTest {

    /**
     * Make sure dependencies introduced by a
     * <a href="https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.7.18">RuntimeVisibleParameterAnnotations Attribute</a>
     * are detected.
     */
    @Test
    public void testAnnotation() throws IOException {
        ClassFile cf = new ClassFile(SpecialArgSample.class);
        assertThat(cf.getDependentClasses()).containsOnly(
                "de.spricom.dessert.test.classfile.samples.SpecialArg",
                "java.io.PrintStream",
                "java.lang.Object",
                "java.lang.String",
                "java.lang.StringBuilder",
                "java.lang.System");
    }

    /**
     * Make sure the bounds of a generic are always added as a dependency, even if it
     * is java.lang.Object. <b>Dessert differs from jdeps, here.</b>
     */
    @Test
    public void testGenericWithoutBounds() throws IOException {
        ClassFile cf = new ClassFile(GenericWithoutBounds.class);
        assertThat(cf.getDependentClasses()).containsOnly(
                "java.lang.Object",
                "de.spricom.dessert.test.classfile.samples.Something");
    }

    /**
     * Make sure implicit the bounds of a generic are always added as an dependency even
     * if there is no field or method using the type argument.
     * <b>Dessert differs from jdeps, here.</b>
     */
    @Test
    public void testGenericWithoutBoundsEmpty() throws IOException {
        ClassFile cf = new ClassFile(GenericWithoutBoundsEmpty.class);
        assertThat(cf.getDependentClasses()).containsOnly(
                "java.lang.Object",
                "de.spricom.dessert.test.classfile.samples.Something");
    }

    /**
     * Make sure the explicit bounds of a generic are always added as an dependency even
     * if there is no field or method using the type argument.
     * <b>Dessert differs from jdeps, here.</b>
     */
    @Test
    public void testEmptyGenericWithBounds() throws IOException {
        ClassFile cf = new ClassFile(EmptyGenericWithBounds.class);
        assertThat(cf.getDependentClasses()).containsOnly(
                "java.lang.Object",
                "java.nio.Buffer");
    }
}
