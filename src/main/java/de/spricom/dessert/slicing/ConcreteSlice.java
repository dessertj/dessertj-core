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

import de.spricom.dessert.util.Predicate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A concrete slice is a concrete collection of classes.
 * Hence it contains a set of {@link Clazz}.
 * The sum or difference on concrete slices
 * produce a concrete slice again.
 */
public class ConcreteSlice extends AbstractSlice implements Concrete {
    public static final ConcreteSlice EMPTY_SLICE = new ConcreteSlice(Collections.<Clazz>emptySet());

    private final Set<Clazz> entries;

    protected ConcreteSlice(Set<Clazz> entries) {
        this.entries = entries;
    }

    @Override
    public ConcreteSlice slice(Predicate<Clazz> predicate) {
        Set<Clazz> filtered = new HashSet<Clazz>();
        for (Clazz entry : entries) {
            if (predicate.test(entry)) {
                filtered.add(entry);
            }
        }
        if (filtered.isEmpty()) {
            return EMPTY_SLICE;
        }
        return new ConcreteSlice(filtered);
    }

    @Override
    public boolean contains(Clazz entry) {
        return entries.contains(entry);
    }

    public Set<Clazz> getClazzes() {
        return entries;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("slice of [");
        Iterator<Clazz> iter = entries.iterator();
        boolean first = true;
        while (iter.hasNext() && sb.length() < 60) {
            Clazz entry = iter.next();
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(entry.getName());
        }
        if (iter.hasNext()) {
            sb.append(" ...");
        }
        sb.append("]");
        return sb.toString();
    }

}
