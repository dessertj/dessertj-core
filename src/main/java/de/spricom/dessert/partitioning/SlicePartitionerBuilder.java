package de.spricom.dessert.partitioning;

import de.spricom.dessert.slicing.Clazz;
import de.spricom.dessert.slicing.SlicePartitioner;
import de.spricom.dessert.util.Assertions;
import de.spricom.dessert.util.Predicate;

import java.util.LinkedList;
import java.util.List;

public class SlicePartitionerBuilder {
    private List<NamedPredicate> predicates = new LinkedList<NamedPredicate>();

    public NamedPredicate split(String name) {
        NamedPredicate predicate = new NamedPredicate(name);
        predicates.add(predicate);
        return predicate;
    }

    public SlicePartitioner build() {
        return new SlicePartitioner() {
            @Override
            public String partKey(Clazz entry) {
                for (NamedPredicate predicate : predicates) {
                    if (predicate != null) {
                        throw new IllegalArgumentException("by() not called for " + predicate);
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
            return SlicePartitionerBuilder.this.build();
        }

        public String toString() {
            return "predicate " + name;
        }
    }
}
