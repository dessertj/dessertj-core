package de.spricom.dessert.slicing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This class implements the Depth-first search algorithm (see <a href=
 * "https://en.wikipedia.org/wiki/Topological_sorting">https://en.wikipedia.org/wiki/Topological_sorting</a>)
 * to detect cyclic dependencies.
 * 
 * @param <T>
 *            The type of object to sort.
 */
public final class DependencyGraph<T> {
    static enum Mark {
        NONE, TEMPORARY, PERMANENT
    }

    static final class Node<T> {
        final T value;
        final Set<Node<T>> directDependencies = new HashSet<>();
        Mark mark = Mark.NONE;

        public Node(T value) {
            Objects.requireNonNull(value, "value");
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

    private Map<T, Node<T>> nodes = new HashMap<>();
    private LinkedList<Node<T>> sorted;

    public void addDependency(T from, T to) {
        sorted = null;
        getNode(from).directDependencies.add(getNode(to));
    }

    private Node<T> getNode(T value) {
        Node<T> n = nodes.get(value);
        if (n == null) {
            n = new Node<T>(value);
            nodes.put(value, n);
        }
        return n;
    }

    public List<T> getSorted() {
        if (sorted == null) {
            sort();
        }
        List<T> list = new ArrayList<>(sorted.size());
        for (Node<T> node : sorted) {
            list.add(node.value);
        }
        return list;
    }

    private void sort() {
        sorted = new LinkedList<>();
        for (Node<T> n : nodes.values()) {
            visit(n);
        }
    }

    private void visit(Node<T> n) {
        if (n.mark == Mark.PERMANENT) {
            return;
        } else if (n.mark == Mark.TEMPORARY) {
            throw new IllegalStateException("Not a DAG");
        } else {
            n.mark = Mark.TEMPORARY;
            for (Node<T> m : n.directDependencies) {
                visit(m);
            }
            n.mark = Mark.PERMANENT;
            sorted.addFirst(n);
        }
    }
}
