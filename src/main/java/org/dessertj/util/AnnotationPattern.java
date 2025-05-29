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

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class AnnotationPattern {

    private final String annotationClassName;
    private final Map<String, Object> annotationMembers;

    private AnnotationPattern(String annotationClassName, Map<String, Object> annotationMembers) {
        Assertions.notNull(annotationClassName, "annotationClassName");
        Assertions.notNull(annotationMembers, "annotationMembers");
        this.annotationClassName = annotationClassName;
        this.annotationMembers = annotationMembers;
    }

    public String getAnnotationClassName() {
        return annotationClassName;
    }

    public Map<String, Object> getAnnotationMembers() {
        return annotationMembers;
    }

    public static AnnotationPattern of(Class<? extends Annotation> annotation) {
        return new AnnotationPattern(annotation.getName(), Collections.<String, Object>emptyMap());
    }

    public static AnnotationPattern of(Class<? extends Annotation> annotation, Map<String, Object> annotationMembers) {
        return new AnnotationPattern(annotation.getName(), annotationMembers);
    }

    public static AnnotationPattern of(Class<? extends Annotation> annotation, Member... members) {
        return new AnnotationPattern(annotation.getName(), members(members));
    }

    public static AnnotationPattern of(String annotationClassName, Member... members) {
        return new AnnotationPattern(annotationClassName, members(members));
    }

    public static Member member(String name, boolean value) {
        return new Member(name, Boolean.valueOf(value));
    }

    public static Member member(String name, boolean... value) {
        Boolean[] boxed = new Boolean[value.length];
        for (int i = 0; i < value.length; i++) {
            boxed[i] = Boolean.valueOf(value[i]);
        }
        return new Member(name, boxed);
    }

    public static Member member(String name, int value) {
        return new Member(name, Integer.valueOf(value));
    }

    public static Member member(String name, int... value) {
        Integer[] boxed = new Integer[value.length];
        for (int i = 0; i < value.length; i++) {
            boxed[i] = Integer.valueOf(value[i]);
        }
        return new Member(name, boxed);
    }

    public static Member member(String name, long value) {
        return new Member(name, Long.valueOf(value));
    }

    public static Member member(String name, long[] value) {
        Long[] boxed = new Long[value.length];
        for (int i = 0; i < value.length; i++) {
            boxed[i] = Long.valueOf(value[i]);
        }
        return new Member(name, boxed);
    }

    public static Member member(String name, float value) {
        return new Member(name, Float.valueOf(value));
    }

    public static Member member(String name, float[] value) {
        Float[] boxed = new Float[value.length];
        for (int i = 0; i < value.length; i++) {
            boxed[i] = Float.valueOf(value[i]);
        }
        return new Member(name, boxed);
    }

    public static Member member(String name, double value) {
        return new Member(name, Double.valueOf(value));
    }

    public static Member member(String name, double[] value) {
        Double[] boxed = new Double[value.length];
        for (int i = 0; i < value.length; i++) {
            boxed[i] = Double.valueOf(value[i]);
        }
        return new Member(name, boxed);
    }

    public static Member member(String name, String value) {
        return new Member(name, value);
    }

    public static Member member(String name, String... value) {
        return new Member(name, value);
    }

    public static Member member(String name, Enum<?> value) {
        return new Member(name, value);
    }

    public static Member member(String name, Enum<?>... value) {
        return new Member(name, value);
    }

    public static Member member(String name, Class<?> value) {
        return new Member(name, value);
    }

    public static Member member(String name, Class<?>... value) {
        return new Member(name, value);
    }

    public static Member member(String name, AnnotationPattern value) {
        return new Member(name, value);
    }

    public static Member member(String name, AnnotationPattern... value) {
        return new Member(name, value);
    }

    private static Map<String, Object> members(Member[] members) {
        Map<String, Object> map = new HashMap<String, Object>(members.length * 4 / 3);
        for (Member member : members) {
            map.put(member.name, member.value);
        }
        return map;
    }

    public static class Member {
        final String name;
        final Object value;

        Member(String name, Object value) {
            this.name = name;
            this.value = value;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(256);
        sb.append("@").append(annotationClassName).append("(");
        boolean first = true;
        for (Map.Entry<String, Object> member : annotationMembers.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(member.getKey()).append("=").append(member.getValue());
        }
        sb.append(")");
        return sb.toString();
    }
}
