package de.spricom.dessert.matching;

/**
 * Matches the "*" wildcard.
 */
class AnyShortNameMatcher extends AbstractShortNameMatcher {

    public AnyShortNameMatcher(ShortNameMatcher[] shortNameMatchers, int index) {
        super(shortNameMatchers, index);
    }

    @Override
    public ShortNameMatcher match(String shortName) {
        return next();
    }

    public String toString() {
        return "*";
    }
}
