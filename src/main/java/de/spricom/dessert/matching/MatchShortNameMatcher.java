package de.spricom.dessert.matching;

enum MatchShortNameMatcher implements ShortNameMatcher {
    MATCH;

    @Override
    public ShortNameMatcher match(String shortName) {
        return MissShortNameMatcher.MISS;
    }

    @Override
    public boolean matches() {
        return true;
    }

    @Override
    public boolean matchesPackage() {
        return false;
    }

    @Override
    public boolean matchPossible() {
        return false;
    }

    @Override
    public ShortNameMatcher next() {
        return this;
    }
}
