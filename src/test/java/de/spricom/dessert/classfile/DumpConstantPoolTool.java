package de.spricom.dessert.classfile;

import org.junit.Test;

import java.io.IOException;

public class DumpConstantPoolTool {

    @Test
    public void test() throws IOException {
        ClassFile cf = new ClassFile(this.getClass());
        System.out.println(cf.dumpConstantPool());
    }
}
