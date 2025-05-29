package org.dessertj.util;

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

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class DagTest {

    @Test
    public void cycleWith2Nodes() {
        Dag<Integer> dag = new Dag<Integer>();
        dag.addEdge(1, 2);
        dag.addEdge(2, 1);
        assertThat(dag.isCycleFree()).isFalse();
        assertThat(dag.cycle()).containsExactly(1, 2, 1);
    }

    @Test
    public void twoCycles() {
        Dag<Integer> dag = new Dag<Integer>();
        dag.addEdge(1, 2);
        dag.addEdge(2, 3);
        dag.addEdge(3, 4);
        dag.addEdge(4, 5);
        dag.addEdge(5, 4);
        dag.addEdge(2, 1);
        assertThat(dag.isCycleFree()).isFalse();
        assertThat(dag.cycle()).containsExactly(1, 2, 1);
    }

    @Test
    public void bigCycle() {
        Dag<Integer> dag = new Dag<Integer>();
        dag.addEdge(1, 2);
        dag.addEdge(2, 3);
        dag.addEdge(3, 4);
        dag.addEdge(4, 5);
        dag.addEdge(5, 1);
        assertThat(dag.isCycleFree()).isFalse();
        assertThat(dag.cycle()).containsExactly(1, 2, 3, 4, 5, 1);
    }

    @Test
    public void disconnected() {
        Dag<Integer> dag = new Dag<Integer>();
        dag.addEdge(1, 2);
        dag.addEdge(2, 3);
        dag.addEdge(3, 1);
        dag.addEdge(4, 5);
        dag.addEdge(5, 4);
        assertThat(dag.isCycleFree()).isFalse();
        assertThat(dag.cycle()).containsExactly(1, 2, 3, 1);
    }

    @Test
    public void duplicates() {
        Dag<Integer> dag = new Dag<Integer>();
        for (int i = 0; i < 3; i++) {
            dag.addEdge(1, 2);
            dag.addEdge(2, 3);
            dag.addEdge(3, 4);
            dag.addEdge(4, 5);
        }
        assertThat(dag.isCycleFree()).isTrue();
    }

    @Test
    public void self() {
        Dag<Integer> dag = new Dag<Integer>();
        for (int i = 1; i <= 3; i++) {
            dag.addEdge(i, i);
        }
        assertThat(dag.isCycleFree()).isFalse();
        assertThat(dag.cycle()).containsExactly(1, 1);
    }
}
