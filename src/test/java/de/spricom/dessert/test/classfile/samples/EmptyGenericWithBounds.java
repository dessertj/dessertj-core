package de.spricom.dessert.test.classfile.samples;

import java.nio.Buffer;

/**
 * According to 'jdeps' this class does have a dependency to {@link Buffer}.
 * Dessert always adds the bounds to the dependencies.
 *
 * @param <T> an object bound by {@link Buffer}
 */
public interface EmptyGenericWithBounds<T extends Buffer> {

}
