package de.spricom.dessert.traversal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JarProcessor implements ClassProcessor {
	private static final Logger log = Logger.getLogger(JarProcessor.class.getName());

	private final File jar;

	public JarProcessor(File jar) {
		if (jar == null) {
			throw new IllegalArgumentException("jar == null");
		}
		if (!jar.isFile()) {
			throw new IllegalArgumentException("jar is not a file");
		}
		this.jar = jar;
	}

	@Override
	public void traverseAllClasses(ClassVisitor visitor) throws IOException {
		if (visitor == null) {
			throw new IllegalArgumentException("visitor == null");
		}
		JarFile jarFile = new JarFile(jar);
		jarFile.stream().filter(e -> !e.isDirectory()).filter(e -> e.getName().endsWith(".class"))
				.forEach(e -> process(e, jarFile, visitor));
	}

	private void process(JarEntry entry, JarFile jarFile, ClassVisitor visitor) {
		String className = entry.getName();
		className = className.substring(0, className.length() - ".class".length());
		className = className.replace('/', '.');
		try (InputStream is = jarFile.getInputStream(entry)) {
			visitor.visit(jar, className, is);
		} catch (IOException ex) {
			log.log(Level.SEVERE, "Cannot process " + entry + " of " + jar, ex);
		}
	}
}
