package de.spricom.dessert.matching;

import de.spricom.dessert.util.Assertions;

/**
 * This pattern identifies a set of classes by their full qualified classname.
 * The syntax has been motivated by the
 * <a href="https://www.eclipse.org/aspectj/doc/released/progguide/quick-typePatterns.html">AspectJ TypeNamePattern</a>
 * with slight modifications:
 * <ul>
 *     <li>The pattern can either be a plain type name, the wildcard *, or an
 *     identifier with embedded * and .. wildcards</li>
 *     <li>An * matches any sequence of characters, but does not match the package
 *     separator "."</li>
 *     <li>An .. matches any sequence of characters that starts and ends with the package
 *     separator "."</li>
 *     <li>The identifier to match with is always the name returned by {@link Class#getName()}. Thus
 *     $ is the only inner-type separator supported.</li>
 *     <li>The * does match $, too.</li>
 *     <li>A leading .. additionally matches the root package.</li>
 * </ul>
 * Examples:
 * <table>
 *     <tr>
 *         <td>sample.Foo</td><td>Matches only sample.Foo</td>
 *     </tr>
 *     <tr>
 *         <td>sample.Foo*</td><td>Matches all types in sample starting with "Foo" and all inner-types of Foo</td>
 *     </tr>
 *     <tr>
 *         <td>sample.Foo$*</td><td>Matches only the inner-types of Foo</td>
 *     </tr>
 *     <tr>
 *         <td>..Foo</td><td>Matches all Foo in any package (incl. root package)</td>
 *     </tr>
 *     <tr>
 *         <td>*.*..Foo</td><td>Matches all Foo nested in a sub-package</td>
 *     </tr>
 *     <tr>
 *         <td>*</td><td>Matches all types in the root package</td>
 *     </tr>
 *     <tr>
 *         <td>..*</td><td>Matches all types</td>
 *     </tr>
 * </table>
 */
public class NamePattern implements Comparable<NamePattern> {
    public static final NamePattern ANY_NAME = new NamePattern(NamePattern.createShortNameMatchers("..*")) {

        @Override
        public boolean matches(String name) {
            return true;
        }

        public NamePattern and(NamePattern other) {
            return other;
        }
    };

    public static final NamePattern NO_NAME = new NamePattern(new ShortNameMatcher[]{MissShortNameMatcher.MISS}) {
        @Override
        public boolean matches(String name) {
            return false;
        }

        public NamePattern and(NamePattern other) {
            return this;
        }
    };

    private final ShortNameMatcher[] shortNameMatchers;

    private NamePattern(ShortNameMatcher[] shortNameMatchers) {
        this.shortNameMatchers = shortNameMatchers;
    }

    public static NamePattern of(String pattern) {
        if ("..*".equals(pattern)) {
            return ANY_NAME;
        }
        validate(pattern);
        ShortNameMatcher[] shortNameMatchers = createShortNameMatchers(pattern);
        return new NamePattern(shortNameMatchers);
    }

    private static ShortNameMatcher[] createShortNameMatchers(String pattern) {
        if (pattern.startsWith("..")) {
            pattern = pattern.substring(1);
        }
        String[] parts = pattern.split("\\.");
        ShortNameMatcher[] shortNameMatchers = new ShortNameMatcher[parts.length];
        for (int i = 0; i < parts.length; i++) {
            shortNameMatchers[i] = createShortNameMatcher(shortNameMatchers, i, parts[i]);
        }
        return shortNameMatchers;
    }

    private static void validate(String pattern) {
        Assertions.notNull(pattern, "pattern");
        if (pattern.contains("...")
                || pattern.endsWith("..")) {
            throw new IllegalArgumentException("invalid pattern: " + pattern);
        }
    }

    private static ShortNameMatcher createShortNameMatcher(ShortNameMatcher[] shortNameMatchers, int i, String part) {
        if ("".equals(part)) {
            return new WildcardShortNameMatcher(shortNameMatchers, i);
        } else if (part.equals("*")) {
            return new AnyShortNameMatcher(shortNameMatchers, i);
        } else if (part.contains("*")) {
            return new RegexShortNameMatcher(shortNameMatchers, i, part);
        } else {
            return new ConstantShortNameMatcher(shortNameMatchers, i, part);
        }
    }

    public NamePattern and(NamePattern other) {
        if (other == ANY_NAME) {
            return this;
        }
        // TODO
        return other;
    }

    public boolean isConstant() {
        for (ShortNameMatcher matcher : shortNameMatchers) {
            if (!(matcher instanceof ConstantShortNameMatcher)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPackageConstant() {
        for (int i = 0; i < shortNameMatchers.length - 1; i++) {
            if (!(shortNameMatchers[i] instanceof ConstantShortNameMatcher)) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllClasses() {
        return shortNameMatchers[shortNameMatchers.length - 1] instanceof AnyShortNameMatcher;
    }

    /**
     * Matches 'name' against the pattern.
     *
     * @param name the identifier to match
     * @return true if the identifier matches
     */
    public boolean matches(String name) {
        String[] parts = name.split("\\.");
        ShortNameMatcher matcher = matcher();
        for (int i = 0; i < parts.length; i++) {
            if (!matcher.isMatchPossible()) {
                return false;
            }
            if (matchesAlternate(parts, i, matcher)) return true;
            matcher = matcher.match(parts[i]);
        }
        return matcher.matches();
    }

    /**
     * @return the first matcher to match the top-level package of the identifier
     */
    public ShortNameMatcher matcher() {
        return shortNameMatchers[0];
    }

    private boolean matches(String[] parts, int index, ShortNameMatcher matcher) {
        for (int i = index; i < parts.length; i++) {
            if (!matcher.isMatchPossible()) {
                break;
            }
            if (matchesAlternate(parts, i, matcher)) return true;
            matcher = matcher.match(parts[i]);
        }
        return matcher.matches();
    }

    private boolean matchesAlternate(String[] parts, int index, ShortNameMatcher matcher) {
        if (!matcher.isWildcard()) {
            return false;
        }
        ShortNameMatcher alternate = matcher.next().match(parts[index]);
        return matches(parts, index + 1, alternate);
    }

    @Override
    public int compareTo(NamePattern o) {
        return Integer.valueOf(shortNameMatchers.length).compareTo(o.shortNameMatchers.length);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (shortNameMatchers[0].isWildcard()) {
            sb.append(".");
        } else {
            sb.append(shortNameMatchers[0]);
        }
        for (int i = 1; i < shortNameMatchers.length; i++) {
            sb.append(".");
            if (!shortNameMatchers[i].isWildcard()) {
                sb.append(shortNameMatchers[i]);
            }
        }
        return sb.toString();
    }
}
