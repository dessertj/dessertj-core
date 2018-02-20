package de.spricom.dessert.test.classfile;

import de.spricom.dessert.classfile.ClassFile;
import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

public class SpecialClassesDependenciesTest {

    @Test
    public void testAnnotation() throws IOException {
        ClassFile cf = new ClassFile(com.sun.javafx.beans.IDProperty.class);
        assertThat(cf.getDependentClasses()).containsOnly(
                "java.lang.Object",
                "java.lang.String",
                "java.lang.annotation.Annotation",
                "java.lang.annotation.Documented",
                "java.lang.annotation.Inherited",
                "java.lang.annotation.Retention",
                "java.lang.annotation.Target"
        );
    }

    @Test
    public void testKeyFrame() throws IOException {
        ClassFile cf = new ClassFile(javafx.animation.KeyFrame.class);
        assertThat(cf.getDependentClasses()).containsOnly(
                "java.lang.AssertionError",
                "java.lang.Class",
                "java.lang.IllegalArgumentException",
                "java.lang.NullPointerException",
                "java.lang.Object",
                "java.lang.String",
                "java.lang.StringBuilder",
                "java.util.Collection",
                "java.util.Collections",
                "java.util.Iterator",
                "java.util.Set",
                "java.util.concurrent.CopyOnWriteArraySet",
                "javafx.animation.KeyValue",
                "javafx.beans.NamedArg",
                "javafx.event.ActionEvent",
                "javafx.event.EventHandler",
                "javafx.util.Duration"
        );
    }
}
