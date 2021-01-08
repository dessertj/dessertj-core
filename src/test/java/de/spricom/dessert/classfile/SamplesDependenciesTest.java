package de.spricom.dessert.classfile;

import de.spricom.dessert.samples.generics.EmptyGenericWithBounds;
import de.spricom.dessert.samples.generics.GenericWithoutBounds;
import de.spricom.dessert.samples.generics.GenericWithoutBoundsEmpty;
import de.spricom.dessert.samples.annotations.SpecialArgSample;
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
                "de.spricom.dessert.samples.annotations.SpecialArg",
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
                "de.spricom.dessert.samples.basic.Something");
    }

    /**
     * Make sure implicit the bounds of a generic are always added as an dependency even
     * if there is no field or method using the type argument.
     * <b>Dessert differs from jdeps, here.</b>
     */
    @Test
    public void testGenericWithoutBoundsEmpty() throws IOException {
        ClassFile cf = new ClassFile(GenericWithoutBoundsEmpty.class);
        System.out.println(cf.dump());
        assertThat(cf.getDependentClasses()).containsOnly(
                "java.lang.Object",
                "de.spricom.dessert.samples.basic.Something");
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
