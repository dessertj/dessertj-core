package de.spricom.dessert.partitioning;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2022 Hans Jörg Heßmann
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.slicing.Clazz;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.util.AnnotationPattern;
import de.spricom.dessert.util.Predicate;
import de.spricom.dessert.util.Predicates;

public final class ClazzPredicates {

    /**
     * This is a catch all predicate that can be used to collect
     * anything that does not match an other predicate.
     */
    public static final Predicate<Clazz> EACH = Predicates.any();

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

    public static final Predicate<Clazz> DEPRECATED = new Predicate<Clazz>() {
        @Override
        public boolean test(Clazz clazz) {
            return clazz.getClassFile().isDeprecated();
        }
    };

    public static final Predicate<Clazz> INNER_TYPE = new Predicate<Clazz>() {
        @Override
        public boolean test(Clazz clazz) {
            if (clazz.getClassFile() != null) {
                return clazz.getClassFile().isInnerClass();
            }
            // Not correct, if classname contains $, but sufficient for the rare case where there is no ClassFile
            return clazz.getName().lastIndexOf('$') != -1;
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

    public static Predicate<Clazz> hostedBy(final Clazz host) {
        return new Predicate<Clazz>() {
            private final Slice nest = host.getNest();

            @Override
            public boolean test(Clazz clazz) {
                return nest.contains(clazz);
            }
        };
    }

    public static Predicate<Clazz> matchesAnnotation(final AnnotationPattern annotationPattern) {
        return matchesClassFile(new AnnotationMatcher(annotationPattern));
    }

    public static Predicate<Clazz> matchesClassFile(final Predicate<ClassFile> classFilePredicate) {
        return new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz clazz) {
                return classFilePredicate.test(clazz.getClassFile());
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
}
