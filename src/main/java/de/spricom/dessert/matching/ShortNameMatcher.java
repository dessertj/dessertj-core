package de.spricom.dessert.matching;

interface ShortNameMatcher {

    ShortNameMatcher match(String shortName);

    boolean matches();

    boolean matchesPackage();

    boolean matchPossible();

    ShortNameMatcher next();
}
