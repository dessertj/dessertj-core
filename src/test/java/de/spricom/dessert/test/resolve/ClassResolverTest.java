package de.spricom.dessert.test.resolve;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.resolve.ClassEntry;
import de.spricom.dessert.resolve.ClassPackage;
import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.resolve.ClassRoot;
import org.fest.assertions.Condition;
import org.junit.Assume;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
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
        assertThat(cp.toString()).isEqualTo("de.spricom.dessert.classfile");
        assertThat(cp.getSubPackages()).hasSize(3);
        assertThat(cp.getParent().getPackageName()).isEqualTo("de.spricom.dessert");

        assertThat(cp.getClasses()).hasSize(5);
        ClassEntry cf = resolver.getClassEntry(ClassFile.class.getName());
        assertThat(cf.getClassfile().getThisClass()).isEqualTo(ClassFile.class.getName());
        assertThat(cf.getPackage()).isSameAs(cp);
        assertThat(cf.getAlternatives()).isNull();

        ClassPackage parent = resolver.getPackage("de.spricom");
        assertThat(parent.getSubPackages()).hasSize(1);

        assertThat(resolver.getPackage("de.spricom.notthere")).isNull();
        assertThat(resolver.getPackage("")).isNotNull().isInstanceOf(ClassRoot.class);
        assertThat(resolver.getRoot(resolver.getRootDirs().iterator().next()).getPackageName()).isEmpty();
    }

    @Test
    public void testAlternativePackages() throws IOException {
        ClassResolver resolver = ClassResolver.ofClassPath();
        Assume.assumeTrue("There are separate directories for productive an test classes",
                resolver.getRootDirs().size() == 2);

        ClassPackage cp1 = resolver.getPackage("de.spricom.dessert");
        assertThat(cp1.getAlternatives()).hasSize(2);

        ClassEntry cf1 = resolver.getClassEntry(this.getClass().getName());
        assertThat(cf1.getClassfile().getThisClass()).isEqualTo(this.getClass().getName());
        assertThat(resolver.getClassEntry(cf1.getPackage().getRootFile(), this.getClass().getName())).isSameAs(cf1);
        assertThat(resolver.getClassEntry(resolver.getPackage(ClassFile.class.getPackage().getName()).getRootFile(), this.getClass().getName())).isNull();
    }

    @Test
    public void testClassPath() throws IOException {
        ClassResolver resolver = ClassResolver.ofClassPath();
        assertThat(resolver.getRootDirs()).isNotEmpty();
        assertThat(resolver.getRootJars()).isNotEmpty();
        assertThat(resolver.getRootDirs().size() + resolver.getRootJars().size()).isEqualTo(resolver.getRootFiles().size());
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
