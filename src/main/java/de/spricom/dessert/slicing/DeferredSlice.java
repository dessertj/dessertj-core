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
import de.spricom.dessert.util.Sets;

import java.util.Set;

class DeferredSlice extends AbstractSlice {
    private final Slice derivedSlice;
    private final ClazzResolver resolver;
    private ConcreteSlice concreteSlice;

    DeferredSlice(Slice derivedSlice, ClazzResolver resolver) {
        this.derivedSlice = derivedSlice;
        this.resolver = resolver;
    }

    @Override
    public Slice combine(final Slice other) {
        if (concreteSlice != null) {
            return concreteSlice.combine(other);
        }
        ClazzResolver combinedResolver = new ClazzResolver() {
            @Override
            public Set<Clazz> getClazzes() {
                return Sets.union(resolver.getClazzes(), other.getClazzes());
            }
        };
        return new DeferredSlice(derivedSlice.combine(other), combinedResolver);
    }

    @Override
    public Slice slice(final Predicate<Clazz> predicate) {
        if (concreteSlice != null) {
            return concreteSlice.slice(predicate);
        }
        return new DeferredSlice(derivedSlice.slice(predicate), resolver);
    }

    @Override
    public boolean contains(Clazz entry) {
        if (concreteSlice != null) {
            return concreteSlice.contains(entry);
        }
        return derivedSlice.contains(entry)
                && getClazzes().contains(entry);
    }

    @Override
    public boolean isIterable() {
        return true;
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
        return concreteSlice.getClazzes();
    }

    public String toString() {
        getClazzes(); // ensure concrete slice is resolved
        return concreteSlice.toString();
    }
}
