package de.spricom.dessert.matching;

/**
 * This matcher will be returned it the latest consumed short-name does not match after the
 * prefix consumed so far. This matcher terminates the matching process for the current branch.
 */
enum MissShortNameMatcher implements ShortNameMatcher {
    MISS;

    @Override
    public ShortNameMatcher match(String shortName) {
        return MISS;
    }

    @Override
    public boolean matches() {
        return false;
    }

    @Override
    public boolean isLast() {
        return false;
    }

    @Override
    public boolean isMatchPossible() {
        return false;
    }

    @Override
    public boolean isWildcard() {
        return false;
    }

    @Override
    public ShortNameMatcher next() {
        return this;
    }
}
