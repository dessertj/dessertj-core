package de.spricom.dessert.util;

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
}
