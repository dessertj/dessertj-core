package de.spricom.dessert.matching;

/**
 * Used for recursive traversal of package trees.
 * It can decide for each branch whether it might be a match.
 * Only if a leaf {@link #matches()} then there is match.
 */
interface ShortNameMatcher {

    /**
     * Returns the result of matching a sub-package name after the branch that has already been matched.
     *
     * @param shortName the package-name without parent package prefix or the class-name without package prefix
     * @return the match result
     */
    ShortNameMatcher match(String shortName);

    /**
     * @return true if the last matcher matched, hence the complete identifier matches
     */
    boolean matches();

    /**
     * @return true if anything but the last matcher matched, hence the complete package-prefix if an
     * identifier matches
     */
    boolean matchesPackage();

    /**
     * @return true if the last match was successful, hence the traversal can continue to the next sub-package
     */
    boolean isMatchPossible();

    /**
     * @return true if the last match was done with a ".." wildcard. Hence the alternative where ".." was no
     * match must be examined, too.
     */
    boolean isMatchUncertain();

    /**
     * @return the following matcher to match the next <i>shortName</i>
     */
    ShortNameMatcher next();
}
