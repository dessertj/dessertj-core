package de.spricom.dessert.util;

public final class Assertions {

    private Assertions() {
    }

    public static <O> O notNull(O obj, String name) {
        if (obj == null) {
            throw new NullPointerException(name);
        }
        return obj;
    }
}
