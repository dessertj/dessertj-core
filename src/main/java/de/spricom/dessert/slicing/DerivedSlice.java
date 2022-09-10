package de.spricom.dessert.slicing;

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

import de.spricom.dessert.matching.NamePattern;
import de.spricom.dessert.resolve.ResolveException;
import de.spricom.dessert.util.Predicate;
import de.spricom.dessert.util.Predicates;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

final class DerivedSlice extends AbstractSlice {
    private final Set<NamePattern> namePatterns;
    private final Predicate<Clazz> predicate;
    private final Set<Clazz> cache = new TreeSet<Clazz>();

    private DerivedSlice(Set<NamePattern> namePatterns, Predicate<Clazz> predicate) {
        this.namePatterns = namePatterns;
        this.predicate = predicate;
    }

    DerivedSlice(NamePattern namePattern) {
        this(Collections.singleton(namePattern), Predicates.<Clazz>any());
    }

    DerivedSlice(Predicate<Clazz> predicate) {
        this(Collections.<NamePattern>emptySet(), predicate);
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
    public Slice slice(String pattern) {
        TreeSet<NamePattern> patterns = new TreeSet<NamePattern>(namePatterns);
        patterns.add(NamePattern.of(pattern));
        return new DerivedSlice(patterns, predicate);
    }

    @Override
    public boolean contains(Clazz entry) {
        if (cache.contains(entry)) {
            return true;
        }
        for (NamePattern namePattern : namePatterns) {
            if (!namePattern.matches(entry.getName())) {
                return false;
            }
        }
        boolean member = predicate.test(entry);
        if (member) {
            cache.add(entry);
        }
        return member;
    }

    @Override
    public Set<Clazz> getClazzes() {
        throw new ResolveException("Cannot get classes for " + this);
    }

    public String toString() {
        if (namePatterns.isEmpty()) {
            return "slice from " + predicate;
        } else if (namePatterns.size() == 1) {
            return "slice " + namePatterns.iterator().next();
        } else {
            return "slice " + namePatterns;
        }
    }
}
