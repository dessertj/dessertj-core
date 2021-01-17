package de.spricom.dessert.partitioning;

import de.spricom.dessert.slicing.Clazz;
import de.spricom.dessert.util.Predicate;

public final class ClazzPredicates {

    /**
     * This is a catch all predicate that can be used to collect
     * anything that does not match an other predicate.
     */
    public static final Predicate<Clazz> EACH = new Predicate<Clazz>() {
        @Override
        public boolean test(Clazz clazz) {
            return true;
        }
    };

    public static final Predicate<Clazz> PUBLIC = new Predicate<Clazz>() {
        @Override
        public boolean test(Clazz clazz) {
            return clazz.getClassFile().isPublic();
        }
    };

    public static final Predicate<Clazz> FINAL = new Predicate<Clazz>() {
        @Override
        public boolean test(Clazz clazz) {
            return clazz.getClassFile().isFinal();
        }
    };

    public static final Predicate<Clazz> SUPER = new Predicate<Clazz>() {
        @Override
        public boolean test(Clazz clazz) {
            return clazz.getClassFile().isSuper();
        }
    };

    public static final Predicate<Clazz> INTERFACE = new Predicate<Clazz>() {
        @Override
        public boolean test(Clazz clazz) {
            return clazz.getClassFile().isInterface();
        }
    };

    public static final Predicate<Clazz> ABSTRACT = new Predicate<Clazz>() {
        @Override
        public boolean test(Clazz clazz) {
            return clazz.getClassFile().isAbstract();
        }
    };

    public static final Predicate<Clazz> SYNTHETIC = new Predicate<Clazz>() {
        @Override
        public boolean test(Clazz clazz) {
            return clazz.getClassFile().isSynthetic();
        }
    };

    public static final Predicate<Clazz> ANNOTATION = new Predicate<Clazz>() {
        @Override
        public boolean test(Clazz clazz) {
            return clazz.getClassFile().isAnnotation();
        }
    };

    public static final Predicate<Clazz> ENUM = new Predicate<Clazz>() {
        @Override
        public boolean test(Clazz clazz) {
            return clazz.getClassFile().isEnum();
        }
    };

    public static Predicate<Clazz> matchesName(final String regex) {
        return new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz clazz) {
                return clazz.getName().matches(regex);
            }
        };
    }

    public static Predicate<Clazz> matchesSimpleName(final String regex) {
        return new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz clazz) {
                return clazz.getSimpleName().matches(regex);
            }
        };
    }

    public static Predicate<Clazz> implementsInterface(final String interfaceName) {
        return new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz clazz) {
                for (String name : clazz.getClassFile().getInterfaces()) {
                    if (name.equals(interfaceName)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static Predicate<Clazz> matches(final Predicate<Class<?>> classPredicate) {
        return new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz clazz) {
                return classPredicate.test(clazz.getClassImpl());
            }
        };
    }

    public static Predicate<Clazz> or(final Predicate<Clazz>... predicates) {
        return new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz clazz) {
                for (Predicate<Clazz> predicate : predicates) {
                    if (predicate.test(clazz)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static Predicate<Clazz> and(final Predicate<Clazz>... predicates) {
        return new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz clazz) {
                for (Predicate<Clazz> predicate : predicates) {
                    if (!predicate.test(clazz)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }
}
