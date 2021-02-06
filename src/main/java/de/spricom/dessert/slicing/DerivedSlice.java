package de.spricom.dessert.slicing;

/*-
 * #%L
 * Dessert Dependency Assertion Library
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
import de.spricom.dessert.util.Predicate;
import de.spricom.dessert.util.Predicates;

import java.util.HashSet;
import java.util.Set;

final class DerivedSlice extends AbstractSlice {
    private final NamePattern namePattern;
    private final Predicate<Clazz> predicate;
    private final Set<Clazz> cache = new HashSet<Clazz>();

    DerivedSlice(NamePattern namePattern, Predicate<Clazz> predicate) {
        this.namePattern = namePattern;
        this.predicate = predicate;
    }

    DerivedSlice(NamePattern namePattern) {
        this(namePattern, Predicates.<Clazz>any());
    }

    DerivedSlice(Predicate<Clazz> predicate) {
        this(NamePattern.ANY_NAME, predicate);
    }

    @Override
    public Slice combine(final Slice other) {
        return new DerivedSlice(new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz clazz) {
                return contains(clazz) || other.contains(clazz);
            }
        });
    }

    @Override
    public Slice slice(final Predicate<Clazz> predicate) {
        return new DerivedSlice(new Predicate<Clazz>() {
            @Override
            public boolean test(Clazz clazz) {
                return contains(clazz) && predicate.test(clazz);
            }
        });
    }

    @Override
    public boolean contains(Clazz entry) {
        if (!namePattern.matches(entry.getName())) {
            return false;
        }
        if (cache.contains(entry)) {
            return true;
        }
        boolean member = predicate.test(entry);
        if (member) {
            cache.add(entry);
        }
        return member;
    }

    @Override
    public boolean isIterable() {
        return false;
    }

    @Override
    public Set<Clazz> getClazzes() {
        throw new UnsupportedOperationException("Cannot materialize DerivedSlice");
    }

    public String toString() {
        return "slice from " + predicate;
    }
}
