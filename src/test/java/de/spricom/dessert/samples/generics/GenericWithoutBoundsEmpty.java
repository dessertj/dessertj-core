package de.spricom.dessert.samples.generics;

import de.spricom.dessert.samples.basic.Something;

/**
 * According to 'jdeps' this class does not have a dependency to java.lang.Object.
 * This is inconsistent with {@link GenericWithoutBounds} without that
 * dependency.
 * Dessert always adds the bounds to the dependencies, even if they are not
 * sepecified explicity, hence they are java.lang.Object.
 *
 * @param <E> any object
 */
public class GenericWithoutBoundsEmpty<E> extends Something {
}
