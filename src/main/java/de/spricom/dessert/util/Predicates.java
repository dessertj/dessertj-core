package de.spricom.dessert.util;

public final class Predicates {
    private Predicates() {};

    public static <T> Predicate<T> any() {
        return new Predicate<T>() {
            @Override
            public boolean test(T t) {
                return true;
            }
        };
    }
}
