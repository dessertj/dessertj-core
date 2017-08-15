package de.spricom.dessert.resolve;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.logging.Logger;

import org.fest.assertions.Condition;
import org.junit.Test;

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
    public void testPerformance() throws IOException {
        assertThat(getDefaultResolver().getPackage("bla.blub")).isNull();
    }

    @Test
    public void test() throws IOException {
        ClassResolver resolver = getDefaultResolver();
        assertThat(resolver.getPackage("java.lang")).isNotNull().is(hasNoAlternative);
        assertThat(resolver.getPackage(getClass().getPackage().getName())).isNotNull().is(hasNoAlternative);
        assertThat(resolver.getPackage("org.springframework.aop.framework")).isNotNull().is(hasNoAlternative);
    }

    @Test
    public void testIOException() throws IOException {
        assertThat(getDefaultResolver().getClassFile(java.io.IOException.class.getName())).isNotNull();
    }
}
