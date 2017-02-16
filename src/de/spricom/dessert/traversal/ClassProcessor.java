package de.spricom.dessert.traversal;

import java.io.IOException;

public interface ClassProcessor {
	void traverseAllClasses(ClassVisitor visitor) throws IOException;
}
