package de.spricom.dessert.matching;

import java.util.regex.Pattern;

class RegexShortNameMatcher extends AbstractShortNameMatcher {

    private final Pattern pattern;

    public RegexShortNameMatcher(ShortNameMatcher[] shortNameMatchers, int index, String wildcardExpression) {
        super(shortNameMatchers, index);
        String regex = wildcardExpression
                .replace("*", ".*")
                .replace("$", "\\$");
        pattern = Pattern.compile(regex);
    }

    @Override
    public ShortNameMatcher match(String shortName) {
        if (pattern.matcher(shortName).matches()) {
            return next();
        }
        return MissShortNameMatcher.MISS;
    }

    public String toString() {
        return pattern.toString()
                .replace("\\$", "$")
                .replace(".*", "*");
    }
}
