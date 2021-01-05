package de.spricom.dessert.samples.exceptions;

import de.spricom.dessert.samples.base.Foo;

public class ThrowSubException {

    private void go() throws BaseException {
        throw new SubException(new Foo());
    }
}
