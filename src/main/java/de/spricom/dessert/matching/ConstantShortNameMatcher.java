package de.spricom.dessert.matching;

class ConstantShortNameMatcher extends AbstractShortNameMatcher {

    private final String name;

    public ConstantShortNameMatcher(ShortNameMatcher[] shortNameMatchers, int index, String name) {
        super(shortNameMatchers, index);
        this.name = name;
    }

    @Override
    public ShortNameMatcher match(String shortName) {
        if (name.equals(shortName)) {
            return next();
        }
        return MissShortNameMatcher.MISS;
    }

    public String toString() {
        return name;
    }
}
