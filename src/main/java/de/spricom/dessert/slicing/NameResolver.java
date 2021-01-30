package de.spricom.dessert.slicing;

import de.spricom.dessert.matching.NamePattern;
import de.spricom.dessert.resolve.ClassEntry;
import de.spricom.dessert.resolve.ClassVisitor;
import de.spricom.dessert.resolve.TraversalRoot;

final class NameResolver extends AbstractClazzResolver implements ClassVisitor {
    private final NamePattern pattern;
    private final TraversalRoot root;

    NameResolver(Classpath cp, NamePattern pattern, TraversalRoot root) {
        super(cp);
        this.pattern = pattern;
        this.root = root;
    }

    NameResolver filter(NamePattern pattern) {
        return new NameResolver(getClasspath(), this.pattern.and(pattern), root);
    }

    @Override
    protected void resolve() {
        root.traverse(pattern, this);
    }

    @Override
    public void visit(ClassEntry ce) {
        add(ce);
    }
}
