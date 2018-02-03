package de.spricom.dessert.test.classfile;

import de.spricom.dessert.classfile.ClassFile;
import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

public class InnerClassesDependenciesTest {

    interface AnInnerInterface {
        void foo();
    }

    class AnInnerClass {
        void foo() throws IOException {
            testThis();
        }
    }

    public static class StaticInnerClass {
        void foo() {
        }
    }

    private Runnable createAnonymousInnerClass() {
        return new Runnable() {
            @Override
            public void run() {
            }
        };
    }

    @Test
    public void testThis() throws IOException {
        ClassFile cf = new ClassFile(getClass());
        assertThat(cf.getDependentClasses()).containsOnly(
                "de.spricom.dessert.classfile.ClassFile",
                "de.spricom.dessert.test.classfile.InnerClassesDependenciesTest$1",
                "de.spricom.dessert.test.classfile.InnerClassesDependenciesTest$AnInnerClass",
                "de.spricom.dessert.test.classfile.InnerClassesDependenciesTest$AnInnerInterface",
                "de.spricom.dessert.test.classfile.InnerClassesDependenciesTest$StaticInnerClass",
                "java.io.IOException",
                "java.lang.Class",
                "java.lang.Object",
                "java.lang.Runnable",
                "java.util.Collection",
                "java.util.Set",
                "org.fest.assertions.Assertions",
                "org.fest.assertions.CollectionAssert",
                "org.junit.Test",
                "java.io.PrintStream",
                "java.lang.String",
                "java.lang.System"
        );
    }

    @Test
    public void testInnerInterface() throws IOException {
        ClassFile cf = new ClassFile(AnInnerInterface.class);
        System.out.println(cf.dumpConstantPool());
        assertThat(cf.getDependentClasses()).containsOnly(
                "java.lang.Object"
        );
    }

    @Test
    public void testInnerClass() throws IOException {
        ClassFile cf = new ClassFile(AnInnerClass.class);
        System.out.println(cf.dumpConstantPool());
        assertThat(cf.getDependentClasses()).containsOnly(
                "de.spricom.dessert.test.classfile.InnerClassesDependenciesTest",
                "java.lang.Object",
                "java.io.IOException"
        );
    }

    @Test
    public void testStaticInnerClass() throws IOException {
        ClassFile cf = new ClassFile(StaticInnerClass.class);
        System.out.println(cf.dumpConstantPool());
        assertThat(cf.getDependentClasses()).containsOnly(
                "java.lang.Object"
        );
    }

    @Test
    public void testAnonymousInnerClass() throws IOException {
        ClassFile cf = new ClassFile(createAnonymousInnerClass().getClass());
        System.out.println(cf.dumpConstantPool());
        assertThat(cf.getDependentClasses()).containsOnly(
                "de.spricom.dessert.test.classfile.InnerClassesDependenciesTest",
                "java.lang.Runnable",
                "java.lang.Object"
        );
    }
}
