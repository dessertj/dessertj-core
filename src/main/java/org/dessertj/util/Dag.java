package org.dessertj.util;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
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

import java.util.*;

/**
 * This class implements the Depth-first search algorithm (see
 * <a href="https://en.wikipedia.org/wiki/Topological_sorting" target="_blank">https://en.wikipedia.org/wiki/Topological_sorting</a>)
 * to detect illegal cycles on a directed acyclic graph.
 *
 * @param <T> The node type.
 */
public final class Dag<T> {
    enum Mark {
        NONE, TEMPORARY, PERMANENT
    }

    static final class Node<T> {
        final T value;
        final Set<Node<T>> edges = new HashSet<Node<T>>();
        Mark mark = Mark.NONE;

        public Node(T value) {
            assert value != null : "value == null";
            this.value = value;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            @SuppressWarnings("unchecked")
            Node<T> other = (Node<T>) obj;
            return value.equals(other.value);
        }
    }

    private final Map<T, Node<T>> nodes = new HashMap<T, Node<T>>();
    private LinkedList<Node<T>> sorted;
    private LinkedList<Node<T>> cycle;

    public void addEdge(T from, T to) {
        sorted = null;
        getNode(from).edges.add(getNode(to));
    }

    private Node<T> getNode(T value) {
        Node<T> n = nodes.get(value);
        if (n == null) {
            n = new Node<T>(value);
            nodes.put(value, n);
        }
        return n;
    }

    public boolean isCycleFree() {
        if (sorted == null) {
            sort();
        }
        return cycle == null;
    }

    public List<T> cycle() {
        return values(cycle);
    }

    private List<T> values(List<Node<T>> nodes) {
        List<T> list = new ArrayList<T>(nodes.size());
        for (Node<T> node : nodes) {
            list.add(node.value);
        }
        return list;
    }

    private void sort() {
        sorted = new LinkedList<Node<T>>();
        for (Node<T> n : nodes.values()) {
            if (visit(n)) {
                return;
            }
        }
    }

    private boolean visit(Node<T> n) {
        if (n.mark == Mark.PERMANENT) {
            return false;
        } else if (n.mark == Mark.TEMPORARY) {
            cycle = new LinkedList<Node<T>>();
            cycle.add(n);
            return true;
        } else {
            n.mark = Mark.TEMPORARY;
            for (Node<T> m : n.edges) {
                if (visit(m)) {
                    cycle.addFirst(n);
                    return true;
                }
            }
            n.mark = Mark.PERMANENT;
            sorted.addFirst(n);
            return false;
        }
    }
}
