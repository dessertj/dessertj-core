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

import java.util.HashSet;
import java.util.Set;

public final class Sets {
    private Sets() {
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

    public static <T> Set<T> difference(Set<T> s, Set<T> t) {
        HashSet<T> r = new HashSet<T>(s);
        r.removeAll(t);
        return r;
    }

    public static <T> Set<T> intersection(Set<T> s, Set<T> t) {
        HashSet<T> r = new HashSet<T>(s);
        r.retainAll(t);
        return r;
    }

    public static <T> Set<T> union(Set<T> s, Set<T> t) {
        HashSet<T> r = new HashSet<T>(s);
        r.addAll(t);
        return r;
    }
}
