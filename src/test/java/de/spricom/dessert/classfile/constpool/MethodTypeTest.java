package de.spricom.dessert.classfile.constpool;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class MethodTypeTest {

    @Test
    public void testIsMethodDescriptor() {
        check("()V");
        check("(LFoo;J)I");
        check("()Lde/spricom/sample/Sample_Test$1;");
        assertThat(MethodType.isMethodDescriptor("(XYZ;)")).isFalse();
    }

    private void check(String descriptor) {
        assertThat(MethodType.isMethodDescriptor(descriptor)).as(descriptor).isTrue();
    }
}
