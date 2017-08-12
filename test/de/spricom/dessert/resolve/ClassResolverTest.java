package de.spricom.dessert.resolve;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.fest.assertions.Condition;
import org.fest.assertions.Fail;
import org.junit.Before;
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
    private ClassResolver resolver;

    @Before
    public void init() throws IOException {
        long ts = System.currentTimeMillis();
        resolver = new ClassResolver();
        log.info("Needed " + (System.currentTimeMillis() - ts) + " ms to initialize ClassResolver.");
    }

    @Test
    public void testPerformance() throws IOException {
        assertThat(resolver.getPackage("bla.blub")).isNull();
    }

    @Test
    public void test() throws IOException {
        assertThat(resolver.getPackage("java.lang")).isNotNull().is(hasNoAlternative);
        assertThat(resolver.getPackage(getClass().getPackage().getName())).isNotNull().is(hasNoAlternative);
        assertThat(resolver.getPackage("org.springframework.aop.framework")).isNotNull().is(hasNoAlternative);
    }

    private File findInPath(String pattern) {
        for (String filename : System.getProperty("java.class.path").split(File.pathSeparator)) {
            if (filename.matches(pattern)) {
                return new File(filename);
            }
        }
        Fail.fail("Thers is no " + pattern + " in classpath");
        return null;
    }
}
