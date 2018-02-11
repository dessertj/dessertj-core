package de.spricom.dessert.test.classfile;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.traversal.ClassVisitor;
import de.spricom.dessert.traversal.PathProcessor;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class JdepsCompatibilityTest implements ClassVisitor {
    private static final File JDK_8_HOME = new File("C:\\Program Files\\Java\\jdk1.8.0_161");
    private static final File JDK_9_HOME = new File("C:\\Program Files\\Java\\jdk-9.0.4");
    private JdepsWrapper wrapper;

    @Before
    public void init() {
        wrapper = new JdepsWrapper(new File("."));
//        wrapper.setJdepsCommand(new File(JDK_8_HOME, "bin/jdeps").getAbsolutePath());
        wrapper.setVerbose(false);
    }

    @Test
    public void testDessert() throws IOException {
        PathProcessor proc = new PathProcessor() {
            @Override
            protected void processJar(File file, ClassVisitor visitor) throws IOException {
                return;
            }

            @Override
            protected void processDirectory(File file, ClassVisitor visitor) throws IOException {
                refresh(file);
                super.processDirectory(file, visitor);
            }
        };
        check(proc);
    }

    @Test
    public void testJarsOnClassPath() throws IOException {
        PathProcessor proc = new PathProcessor() {
            @Override
            protected void processJar(File file, ClassVisitor visitor) throws IOException {
                refresh(file);
                super.processJar(file, visitor);
            }

            @Override
            protected void processDirectory(File file, ClassVisitor visitor) throws IOException {
                return;
             }
        };
        check(proc);
    }

    private void check(PathProcessor proc) throws IOException {
        proc.traverseAllClasses(this);
    }

    private void refresh(File path) {
        System.out.println("Checking " + path);
        wrapper.setPath(path);
        try {
            wrapper.refresh();
        } catch (IOException ex) {
            throw new RuntimeException("Processing " + path + " failed.", ex);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void visit(File root, String classname, InputStream content) {
        try {
            ClassFile cf = new ClassFile(content);
            Set<String> deps1 = cf.getDependentClasses();
            Set<String> deps2 = wrapper.getDependencies(classname);
            assertThat(deps1).as("Dependencies of " + classname).isEqualTo(deps2);
        } catch (IOException ex) {
            throw new RuntimeException("Processing " + classname + " in " + root.getAbsolutePath() + " failed.", ex);
        }
    }
}
