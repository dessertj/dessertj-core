package de.spricom.dessert.slicing;

import de.spricom.dessert.matching.NamePattern;
import de.spricom.dessert.resolve.ClassEntry;
import de.spricom.dessert.resolve.ClassVisitor;
import de.spricom.dessert.resolve.TraversalRoot;
import de.spricom.dessert.util.Predicate;

final class NameResolver extends AbstractClazzResolver implements ClassVisitor {
    private static final Predicate<String> MATCH_ANY = new Predicate<String>() {
        @Override
        public boolean test(String s) {
            return true;
        }
    };

    private final NamePattern pattern;
    private final TraversalRoot root;
    private final Predicate<String> classNamePredicate;

    NameResolver(Classpath cp, NamePattern pattern, TraversalRoot root) {
        super(cp);
        this.pattern = pattern;
        this.root = root;
        this.classNamePredicate = MATCH_ANY;
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
        if (classNamePredicate.test(ce.getClassname())) {
            add(ce);
        }
    }
}
