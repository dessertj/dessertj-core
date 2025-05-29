package org.dessertj.slicing;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2025 Hans Jörg Heßmann
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

import org.dessertj.matching.NamePattern;
import org.dessertj.util.Predicate;

import java.util.Set;
import java.util.TreeSet;

class DeferredSlice extends AbstractSlice {
    private final Slice derivedSlice;
    private final NamePatternClazzResolver resolver;
    private final boolean resolvesConcrete;
    private final Set<Clazz> cache = new TreeSet<Clazz>();
    private ConcreteSlice concreteSlice;

    DeferredSlice(Slice derivedSlice, NamePatternClazzResolver resolver, boolean resolvesConcrete) {
        this.derivedSlice = derivedSlice;
        this.resolver = resolver;
        this.resolvesConcrete = resolvesConcrete;
    }

    @Override
    public Slice slice(String pattern) {
        if (isConcrete()) {
            return concreteSlice.slice(pattern);
        }
        return new DeferredSlice(derivedSlice.slice(pattern),
                resolver.filtered(NamePattern.of(pattern)),
                resolvesConcrete);
    }

    @Override
    public Slice slice(final Predicate<Clazz> predicate) {
        if (isConcrete()) {
            return concreteSlice.slice(predicate);
        }
        return new DeferredSlice(derivedSlice.slice(predicate), resolver, resolvesConcrete);
    }

    @Override
    public boolean contains(Clazz clazz) {
        if (isConcrete()) {
            return concreteSlice.contains(clazz);
        }
        if (cache.contains(clazz)) {
            return true;
        }
        if (!derivedSlice.contains(clazz)) {
            return false;
        }
        if (!resolvesConcrete) {
            // derived slices are not limited by a resolver
            return true;
        }
        NamePatternClazzResolver singleClassResolver = resolver.replace(NamePattern.of(clazz.getName()));
        boolean member = singleClassResolver.getClazzes().contains(clazz);
        if (member) {
            cache.add(clazz);
        }
        return member;
    }

    private boolean isConcrete() {
        return concreteSlice != null
                && (!concreteSlice.getClazzes().isEmpty() || resolvesConcrete);
    }

    @Override
    public Set<Clazz> getClazzes() {
        if (concreteSlice == null) {
            ConcreteSlice cs = new ConcreteSlice(resolver.getClazzes());
            concreteSlice = cs.slice(new Predicate<Clazz>() {
                @Override
                public boolean test(Clazz clazz) {
                    return derivedSlice.contains(clazz);
                }
            });
        }
        cache.clear();
        return concreteSlice.getClazzes();
    }

    public String toString() {
        if (concreteSlice != null) {
            return concreteSlice.toString();
        }
        return derivedSlice.toString();
    }
}
