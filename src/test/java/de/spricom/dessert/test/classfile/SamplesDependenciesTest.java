package de.spricom.dessert.test.classfile;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.test.classfile.samples.SpecialArgSample;
import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

public class SamplesDependenciesTest {

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

}
