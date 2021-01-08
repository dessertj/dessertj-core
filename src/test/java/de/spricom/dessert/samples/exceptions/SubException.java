package de.spricom.dessert.samples.exceptions;

import de.spricom.dessert.samples.basic.Foo;

public class SubException extends BaseException {
    private final Foo foo;

    public SubException(Foo foo) {
        this.foo = foo;
    }
}
