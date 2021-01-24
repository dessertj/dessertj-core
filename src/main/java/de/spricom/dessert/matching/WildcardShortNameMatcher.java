package de.spricom.dessert.matching;

/**
 * Matches the ".." package wildcard.
 */
class WildcardShortNameMatcher extends AbstractShortNameMatcher {

    public WildcardShortNameMatcher(ShortNameMatcher[] shortNameMatchers, int index) {
        super(shortNameMatchers, index);
    }

    @Override
    public ShortNameMatcher match(String shortName) {
        if (next().match(shortName).matchPossible()) {
            return next().next();
        }
        return this;
    }
}
