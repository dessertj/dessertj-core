package de.spricom.dessert.classfile.samples;

/**
 * According to 'jdeps' this class does have a dependency to java.lang.Object.
 * This is inconsistent with {@link GenericWithoutBoundsEmpty} without that
 * dependency.
 * Dessert always adds the bounds to the dependencies, even if they are not
 * sepecified explicity, hence they are java.lang.Object.
 *
 * @param <E> any generic
 */
public class GenericWithoutBounds<E> extends Something {
    private E anObject;

    public E getAnObject() {
        return anObject;
    }

    public void setAnObject(E anObject) {
        this.anObject = anObject;
    }

    public <F> F convert(E anObject) {
        return (F)anObject;
    }
}
