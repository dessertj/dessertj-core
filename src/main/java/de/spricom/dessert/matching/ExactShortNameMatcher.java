package de.spricom.dessert.matching;

class ExactShortNameMatcher extends AbstractShortNameMatcher {

    private final String exactName;

    public ExactShortNameMatcher(ShortNameMatcher[] shortNameMatchers, int index, String exactName) {
        super(shortNameMatchers, index);
        this.exactName = exactName;
    }

    @Override
    public ShortNameMatcher match(String shortName) {
        if (exactName.equals(shortName)) {
            return next();
        }
        return MissShortNameMatcher.MISS;
    }
}
