package de.spricom.dessert.slicing;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2021 Hans Jörg Heßmann
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

import de.spricom.dessert.matching.NamePattern;
import de.spricom.dessert.resolve.TraversalRoot;
import de.spricom.dessert.util.Predicate;

import java.util.Set;

public abstract class AbstractRootSlice extends AbstractSlice {
    private final TraversalRoot traversalRoot;
    private ConcreteSlice concreteSlice;

    AbstractRootSlice(TraversalRoot traversalRoot) {
        this.traversalRoot = traversalRoot;
    }

    public Slice packageOf(Class<?> clazz) {
        return packageOf(clazz.getPackage());
    }

    public Slice packageOf(Package pkg) {
        return packageOf(pkg.getName());
    }

    public Slice packageOf(String packageName) {
        return slice(packageName + ".*");
    }

    public Slice packageTreeOf(Class<?> clazz) {
        return packageTreeOf(clazz.getPackage());
    }

    public Slice packageTreeOf(Package pkg) {
        return packageTreeOf(pkg.getName());
    }

    public Slice packageTreeOf(String packageName) {
        return slice(packageName + "..*");
    }

    @Override
    public Slice slice(String pattern) {
        if (concreteSlice != null) {
            Slice slice = concreteSlice.slice(pattern);
            if (slice.getClazzes().isEmpty()) {
                if (isConcrete()) {
                    return Slices.EMPTY_SLICE;
                } else {
                    return new DerivedSlice(NamePattern.of(pattern));
                }
            }
            return slice;
        }
        NamePattern namePattern = NamePattern.of(pattern);
        return new DeferredSlice(new DerivedSlice(namePattern), resolver(namePattern), isConcrete());
    }

    @Override
    public Slice slice(final Predicate<Clazz> predicate) {
        if (concreteSlice != null) {
            return concreteSlice.slice(predicate);
        }
        return new DeferredSlice(new DerivedSlice(predicate), resolver(NamePattern.ANY_NAME), isConcrete());
    }

    @Override
    public boolean contains(Clazz clazz) {
        if (concreteSlice != null) {
            return concreteSlice.contains(clazz);
        }
        NamePattern name = NamePattern.of(clazz.getName());
        return resolver(name).getClazzes().contains(clazz);
    }

    @Override
    public Set<Clazz> getClazzes() {
        return getConcreteSlice().getClazzes();
    }

    private ConcreteSlice getConcreteSlice() {
        if (concreteSlice == null) {
            concreteSlice = new ConcreteSlice(resolver().getClazzes());
        }
        return concreteSlice;
    }

    private NamePatternClazzResolver resolver() {
        return resolver(NamePattern.ANY_NAME);
    }

    private NamePatternClazzResolver resolver(NamePattern namePattern) {
        return new NamePatternClazzResolver(getClasspath(), namePattern, traversalRoot);
    }

    abstract Classpath getClasspath();

    abstract boolean isConcrete();
}
