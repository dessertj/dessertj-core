package de.spricom.dessert.slicing;

/**
 * Factory methods for {@link Slice}.
 */
public final class Slices {

    private Slices() {
    }

    /**
     * Returns a new {@link Slice} that resembels the union of the <i>slices</i>.
     * Returns an empty slice if <i>slices</i> is empty.
     *
     * @param slices the slices to create the union from
     * @return the union of the the slices
     */
    public static Slice of(Slice... slices) {
        Slice sum = new EmptySlice();
        for (Slice slice : slices) {
            sum = sum.with(slice);
        }
        return sum;
    }
}
