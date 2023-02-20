package de.spricom.dessert.assertions;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2023 Hans Jörg Heßmann
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

import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.util.Dag;

/**
 * Functional interface to render a detected cycle.
 */
public interface CycleRenderer {

    /**
     * Creates a string representation of the detected cycle to
     * be used as message for an {@link AssertionError}.
     *
     * @param dag the DAG that contains the cycle
     * @return the message
     */
    String renderCycle(Dag<Slice> dag);
}
