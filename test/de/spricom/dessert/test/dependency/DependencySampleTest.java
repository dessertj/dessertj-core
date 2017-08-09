package de.spricom.dessert.test.dependency;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;

import de.spricom.dessert.Environment;
import de.spricom.dessert.classfile.attribute.Annotation;
import de.spricom.dessert.classfile.constpool.ConstantPool;
import de.spricom.dessert.dependency.DependencyAssert;

public class DependencySampleTest {
    private static final Logger log = Logger.getLogger(DependencySampleTest.class.getName());

    @Test
    public void testSuccess() throws IOException {
        long ts = System.currentTimeMillis();
        Environment env = new Environment();
        log.info("Needed " + (System.currentTimeMillis() - ts) + " ms to build testSuccess environment.");
        
        ts = System.currentTimeMillis();
        DependencyAssert da = env.assertThat(e -> e.getClassName().startsWith(ConstantPool.class.getPackage().getName()));
        log.info("Needed " + (System.currentTimeMillis() - ts) + " ms for 1st rule");
        
        ts = System.currentTimeMillis();
        da = env.assertThat(e -> e.getClassName().startsWith(Annotation.class.getPackage().getName()));
        log.info("Needed " + (System.currentTimeMillis() - ts) + " ms for 2nd rule");
    }

    @Test
    public void testFailure() throws IOException {
        long ts = System.currentTimeMillis();
        Environment env = new Environment();
        log.info("Needed " + (System.currentTimeMillis() - ts) + " ms to build testFailure environment.");
        
        ts = System.currentTimeMillis();
        DependencyAssert da = env.assertThat(e -> e.getClassName().startsWith(Annotation.class.getPackage().getName()));
        log.info("Needed " + (System.currentTimeMillis() - ts) + " ms for 1st rule");
        
        ts = System.currentTimeMillis();
        da = env.assertThat(e -> e.getClassName().startsWith(ConstantPool.class.getPackage().getName()));
        log.info("Needed " + (System.currentTimeMillis() - ts) + " ms for 2nd rule");
    }
}
