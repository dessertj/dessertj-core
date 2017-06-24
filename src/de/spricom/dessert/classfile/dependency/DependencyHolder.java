package de.spricom.dessert.classfile.dependency;

import java.util.Set;

public interface DependencyHolder {
	void addDependentClassNames(Set<String> classNames);
}
