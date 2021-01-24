package de.spricom.dessert.matching;

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
