package de.spricom.dessert.classfile.constpool;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class FieldTypeTest {

    @Test
    public void testIsFieldDescriptor() {
        check("B");
        check("[[[[Z");
        check("[Lde/spricom/sample/Sample_Test$1;");
        assertThat(FieldType.isFieldDescriptor("Lde/spricom/Test")).isFalse();
    }

    private void check(String descriptor) {
        assertThat(FieldType.isFieldDescriptor(descriptor)).as(descriptor).isTrue();
    }
}
