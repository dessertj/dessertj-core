package de.spricom.dessert.resolve;

import de.spricom.dessert.matching.NamePattern;

public interface TraversalRoot {
    void traverse(NamePattern pattern, ClassVisitor visitor);
}
