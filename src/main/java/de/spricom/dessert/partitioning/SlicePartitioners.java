package de.spricom.dessert.partitioning;

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

import de.spricom.dessert.slicing.SlicePartitioner;

import static de.spricom.dessert.partitioning.ClazzPredicates.*;
import static de.spricom.dessert.util.Predicates.or;

public class SlicePartitioners {

    public static final SlicePartitioner INTERFACES = new SlicePartitionerBuilder()
            .split("interfaces, enums an annotations")
            .by(or(INTERFACE, ENUM, ANNOTATION))
            .split("anything else")
            .by(or(EACH))
            .build();

    public static final SlicePartitioner PUBLIC = new SlicePartitionerBuilder()
            .split("public classes")
            .by(or(ClazzPredicates.PUBLIC))
            .split("package internal classes")
            .by(or(EACH))
            .build();
}
