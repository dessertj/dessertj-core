package org.dessertj.partitioning;

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

import org.dessertj.slicing.Clazz;
import org.dessertj.slicing.SlicePartitioner;
import org.dessertj.util.Assertions;
import org.dessertj.util.Predicate;

import java.util.LinkedList;
import java.util.List;

public class SlicePartitionerBuilder {
    private final List<NamedPredicate> predicates = new LinkedList<NamedPredicate>();

    public NamedPredicate split(String name) {
        NamedPredicate predicate = new NamedPredicate(name);
        predicates.add(predicate);
        return predicate;
    }

    public SlicePartitioner build() {
        if (predicates.isEmpty()) {
            throw new IllegalArgumentException("No predicates given!");
        }
        return new SlicePartitioner() {
            @Override
            public String partKey(Clazz clazz) {
                for (NamedPredicate predicate : predicates) {
                    if (predicate.predicate.test(clazz)) {
                        return predicate.name;
                    }
                }
                return null;
            }
        };
    }

    class NamedPredicate {
        final String name;
        Predicate<Clazz> predicate;

        NamedPredicate(String name) {
            Assertions.notNull(name, "name");
            this.name = name;
        }

        public NamedPredicate by(Predicate<Clazz> predicate) {
            Assertions.notNull(predicate, "predicate");
            if (this.predicate != null) {
                throw new IllegalArgumentException("by() called twice for " + this);
            }
            this.predicate = predicate;
            return this;
        }

        public NamedPredicate split(String name) {
            return SlicePartitionerBuilder.this.split(name);
        }

        public SlicePartitioner build() {
            if (predicate == null) {
                throw new IllegalArgumentException("by() not called for " + this);
            }
            return SlicePartitionerBuilder.this.build();
        }

        public String toString() {
            return "predicate " + name;
        }
    }
}
