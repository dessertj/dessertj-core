package de.spricom.dessert.resolve;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.fest.assertions.Fail;
import org.junit.Before;
import org.junit.Test;

public class ClassResolverTest {
	private ClassResolver resolver;
	
	@Before
	public void init() throws IOException {
	    resolver = new ClassResolver();
	}
	
	@Test
	public void testPerformance() throws IOException {
		assertThat(resolver.getPackage("bla.blub")).isEmpty();
	}

	@Test
    public void test() throws IOException {
        assertThat(resolver.getPackage("java.lang")).hasSize(1);
        assertThat(resolver.getPackage(getClass().getPackage().getName())).hasSize(1);
        assertThat(resolver.getPackage("org.springframework.aop.framework")).hasSize(1);
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
