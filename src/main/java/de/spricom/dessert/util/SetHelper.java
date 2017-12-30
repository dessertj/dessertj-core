package de.spricom.dessert.util;

import java.util.HashSet;
import java.util.Set;

public final class SetHelper {
    private SetHelper() {
    }
    
    public static <T> boolean containsAny(Set<T> s, Set<T> t) {
        for (T v : t) {
            if (s.contains(v)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean containsAll(Set<T> s, Set<T> t) {
        for (T v : t) {
            if (!s.contains(v)) {
                return false;
            }
        }
        return true;
    }

    public static <T> Set<T> subtract(Set<T> s, Set<T> t) {
        HashSet<T> r = new HashSet<T>(s);
        r.removeAll(t);
        return r;
    }

    public static <T> Set<T> intersect(Set<T> s, Set<T> t) {
        HashSet<T> r = new HashSet<T>(s);
        r.retainAll(t);
        return r;
    }
}
