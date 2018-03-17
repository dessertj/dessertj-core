package de.spricom.dessert.test.resolve;

import de.spricom.dessert.resolve.ClassPackage;
import de.spricom.dessert.resolve.ClassResolver;
import org.fest.assertions.Condition;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Logger;

import static org.fest.assertions.Assertions.assertThat;

public class ClassResolverTest {
    private static Logger log = Logger.getLogger(ClassResolverTest.class.getName());
    private static ClassResolver defaultResolver;

    @Test
    public void testClassPathWithoutJars() throws IOException {
        ClassResolver resolver = ClassResolver.ofClassPathWithoutJars();
        assertThat(resolver.getRootFiles()).isEqualTo(resolver.getRootDirs());
        // If there are separate directories for productive an test classes then we have two root dirs.
        assertThat(resolver.getRootDirs().size()).isLessThanOrEqualTo(2);
        assertThat(resolver.getRootJars()).isEmpty();

        ClassPackage cp = resolver.getPackage("de.spricom.dessert.classfile");
        assertThat(cp.getPackageName()).isEqualTo("de.spricom.dessert.classfile");
        assertThat(cp.getSubPackages()).hasSize(3);
        assertThat(cp.getParent().getPackageName()).isEqualTo("de.spricom.dessert");
    }

    @Test
    public void testClassPath() throws IOException {
        ClassResolver resolver = ClassResolver.ofClassPath();
        assertThat(resolver.getRootDirs()).isNotEmpty();
        assertThat(resolver.getRootJars()).isNotEmpty();
        assertThat(resolver.getRootDirs().size() + resolver.getRootJars().size()).isEqualTo(resolver.getRootFiles().size());
        System.out.println(resolver.getRootJars());
        assertThat(resolver.getRootJars()).is(new ContainsFile("junit-4.12.jar"));
    }

    static class ContainsFile extends Condition<Collection<?>> {
        private final String filename;

        ContainsFile(String filename) {
            super("does not contain file " + filename);
            this.filename = filename;
        }

        @Override
        public boolean matches(Collection<?> files) {
            for (Object file : files) {
                if (filename.equals(((File)file).getName())) {
                    return true;
                }
            }
            return false;
        }
    }
}
