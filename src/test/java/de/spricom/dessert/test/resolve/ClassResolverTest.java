package de.spricom.dessert.test.resolve;

import de.spricom.dessert.resolve.ClassPackage;
import de.spricom.dessert.resolve.ClassResolver;
import org.fest.assertions.Condition;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.fest.assertions.Assertions.assertThat;

public class ClassResolverTest {
    private static final Condition<Object> hasNoAlternative = new Condition<Object>("has no alternative") {

        @Override
        public boolean matches(Object obj) {
            if (obj instanceof ClassPackage) {
                return ((ClassPackage) obj).getAlternative() == null;
            }
            return false;
        }
    };

    private static Logger log = Logger.getLogger(ClassResolverTest.class.getName());
    private static ClassResolver defaultResolver;

    private static ClassResolver getDefaultResolver() throws IOException {
        if (defaultResolver == null) {
            long ts = System.currentTimeMillis();
            defaultResolver = ClassResolver.ofClassPathAndBootClassPath();
            log.info("Needed " + (System.currentTimeMillis() - ts) + " ms to initialize resolver for "
                    + defaultResolver.getClassCount() + " classes in " + defaultResolver.getPackageCount()
                    + " packages.");
        }
        return defaultResolver;
    }

    @Test
    public void testClassPathWithoutJars() throws IOException {
        ClassResolver resolver = ClassResolver.ofClassPathWithoutJars();
        assertThat(resolver.getRootFiles()).isEqualTo(resolver.getRootDirs());
        // If there are separate directories for productive an test classes then we have two root dirs.
        assertThat(resolver.getRootDirs().size()).isLessThanOrEqualTo(2);
    }
}
