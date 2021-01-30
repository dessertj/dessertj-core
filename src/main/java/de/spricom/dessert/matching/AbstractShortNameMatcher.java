package de.spricom.dessert.matching;

abstract class AbstractShortNameMatcher implements ShortNameMatcher {

    private final ShortNameMatcher[] shortNameMatchers;
    private final int index;

    protected AbstractShortNameMatcher(ShortNameMatcher[] shortNameMatchers, int index) {
        this.shortNameMatchers = shortNameMatchers;
        this.index = index;
    }

    public ShortNameMatcher next() {
        int next = index + 1;
        if (next == shortNameMatchers.length) {
            // completely matched => matches() == true
            return MatchShortNameMatcher.MATCH;
        }
        return shortNameMatchers[next];
    }

    @Override
    public boolean isWildcard() {
        return false;
    }

    @Override
    public boolean isLast() {
        return index + 1 == shortNameMatchers.length;
    }

    @Override
    public boolean isMatchPossible() {
        return true;
    }

    @Override
    public boolean matches() {
        return false;
    }
}
