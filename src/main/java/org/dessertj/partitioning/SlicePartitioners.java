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

import static org.dessertj.partitioning.ClazzPredicates.ANNOTATION;
import static org.dessertj.partitioning.ClazzPredicates.EACH;
import static org.dessertj.partitioning.ClazzPredicates.ENUM;
import static org.dessertj.partitioning.ClazzPredicates.INTERFACE;
import static org.dessertj.partitioning.ClazzPredicates.SYNTHETIC;
import static org.dessertj.util.Predicates.and;
import static org.dessertj.util.Predicates.not;
import static org.dessertj.util.Predicates.or;

public class SlicePartitioners {

    public static final SlicePartitioner INTERFACES = new SlicePartitionerBuilder()
            .split("interfaces, enums an annotations")
            .by(and(or(INTERFACE, ENUM, ANNOTATION), not(SYNTHETIC)))
            .split("anything else")
            .by(or(EACH))
            .build();

    public static final SlicePartitioner PUBLIC = new SlicePartitionerBuilder()
            .split("public classes")
            .by(or(ClazzPredicates.PUBLIC))
            .split("package internal classes")
            .by(or(EACH))
            .build();

    public static final SlicePartitioner HOST = new SlicePartitioner() {

        @Override
        public String partKey(Clazz entry) {
            return entry.getHost().getName();
        }
    };
}
