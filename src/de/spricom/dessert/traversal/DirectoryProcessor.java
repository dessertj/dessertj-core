package de.spricom.dessert.traversal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DirectoryProcessor implements ClassProcessor {

	private final File root;

	public DirectoryProcessor(File dir) {
		if (dir == null) {
			throw new IllegalArgumentException("dir == null");
		}
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException("dir is not a directory");
		}
		root = dir;
	}

	@Override
	public void traverseAllClasses(ClassVisitor visitor) throws IOException {
		if (visitor == null) {
			throw new IllegalArgumentException("visitor == null");
		}
		traverse(root, null, visitor);
	}

	private void traverse(File dir, String packageName, ClassVisitor visitor) throws IOException {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				traverse(file, concat(packageName, file.getName()), visitor);
			} else if (file.isFile() && file.getName().endsWith(".class")) {
				try (InputStream is = new FileInputStream(file)) {
					visitor.visit(root, concat(packageName, className(file)), is);
				}
			}
		}
	}

	private String concat(String parentPackageName, String name) {
		if (parentPackageName == null) {
			return name;
		}
		return parentPackageName + "." + name;
	}

	private String className(File file) {
		String name = file.getName();
		return name.substring(0, name.length() - ".class".length());
	}
}
