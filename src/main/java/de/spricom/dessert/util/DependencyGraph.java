package de.spricom.dessert.util;

import java.util.*;

/**
 * This class implements the Depth-first search algorithm (see <a href=
 * "https://en.wikipedia.org/wiki/Topological_sorting">https://en.wikipedia.org/wiki/Topological_sorting</a>)
 * to detect cyclic dependencies.
 *
 * @param <T> The type of object to sort.
 */
public final class DependencyGraph<T> {
    static enum Mark {
        NONE, TEMPORARY, PERMANENT
    }

    static final class Node<T> {
        final T value;
        final Set<Node<T>> directDependencies = new HashSet<Node<T>>();
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

    private Map<T, Node<T>> nodes = new HashMap<T, Node<T>>();
    private LinkedList<Node<T>> sorted;
    private LinkedList<Node<T>> cycle;

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

    public boolean isCycleFree() {
        if (sorted == null) {
            sort();
        }
        return cycle == null;
    }

    public List<T> getSorted() {
        if (sorted == null) {
            sort();
        }
        if (cycle != null) {
            throw new IllegalStateException("Not a DAG, cycle: " + getCycle());
        }
        return values(sorted);
    }

    public List<T> getCycle() {
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
            ;
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
            for (Node<T> m : n.directDependencies) {
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
