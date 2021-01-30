package de.spricom.dessert.matching;

/**
 * This matcher will be returned after there is a match for each
 * {@link ShortNameMatcher}. Thus at this node the branch matches,
 * hence we have a match this node is a leaf.
 */
enum MatchShortNameMatcher implements ShortNameMatcher {
    MATCH;

    /**
     * Returns a {@link MissShortNameMatcher#MISS}, because if there
     * is something after the match it's no match anymore. Hence
     * the current match was not a leaf.
     */
    @Override
    public ShortNameMatcher match(String shortName) {
        return MissShortNameMatcher.MISS;
    }

    @Override
    public boolean matches() {
        return true;
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
