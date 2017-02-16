package de.spricom.dessert.test.traversal;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

import de.spricom.dessert.traversal.ClassVisitor;
import de.spricom.dessert.traversal.DirectoryProcessor;
import de.spricom.dessert.traversal.JarProcessor;
import de.spricom.dessert.traversal.PathProcessor;

public class ClassProcessorTest implements ClassVisitor {

	private Class<?> classToCheck;
	private boolean found;

	@Test
	public void testTraverseTestDirectory() throws IOException {
		classToCheck = getClass();

		URL root = classToCheck.getResource("/");
		System.out.println(root);
		DirectoryProcessor proc = new DirectoryProcessor(new File(root.getPath()));
		proc.traverseAllClasses(this);
		assertThat(found).isTrue();
	}

	@Test
	public void testTraverseJarArchive() throws IOException {
		classToCheck = Test.class;

		URL root = classToCheck.getResource("Test.class");
		String path = root.getPath();
		assertThat(path).startsWith("file:/");
		String suffix = ".jar!/" + Test.class.getCanonicalName().replace('.', '/') + ".class";
		assertThat(path).endsWith(suffix);
		File jarFile = new File(path.substring("file:".length(), path.length() - suffix.length() + ".jar".length()));
		System.out.println(jarFile.getAbsolutePath());
		assertThat(jarFile).exists().isFile();

		JarProcessor proc = new JarProcessor(jarFile);
		proc.traverseAllClasses(this);
		assertThat(found).isTrue();
	}

	@Test
	public void testTraverseSystemClassPath() throws IOException {
		classToCheck = ApplicationContext.class;

		PathProcessor proc = new PathProcessor();
		proc.traverseAllClasses(this);
		assertThat(found).isTrue();
	}

	@Override
	public void visit(File root, String classname, InputStream content) {
		System.out.println(classname);
		if (classToCheck.getName().equals(classname)) {
			found = true;
		}
	}
}
