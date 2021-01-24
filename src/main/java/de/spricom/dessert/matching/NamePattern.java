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
public final class NamePattern {

    private final ShortNameMatcher[] shortNameMatchers;

    private NamePattern(ShortNameMatcher[] shortNameMatchers) {
        this.shortNameMatchers = shortNameMatchers;
    }

    public static NamePattern of(String pattern) {
        Assertions.notNull(pattern, "pattern");
        if (pattern.startsWith("..")) {
            pattern = pattern.substring(1);
        }
        String[] parts = pattern.split("\\.");
        ShortNameMatcher[] shortNameMatchers = new ShortNameMatcher[parts.length];
        for (int i = 0; i < parts.length; i++) {
            shortNameMatchers[i] = createShortNameMatcher(shortNameMatchers, i, parts[i]);
        }
        return new NamePattern(shortNameMatchers);
    }

    private static ShortNameMatcher createShortNameMatcher(ShortNameMatcher[] shortNameMatchers, int i, String part) {
        if ("".equals(part)) {
            return new WildcardShortNameMatcher(shortNameMatchers, i);
        } else if (part.contains("*")) {
            return new RegexShortNameMatcher(shortNameMatchers, i, part);
        } else {
            return new ExactShortNameMatcher(shortNameMatchers, i, part);
        }
    }

    public boolean matches(String name) {
        String[] parts = name.split("\\.");
        ShortNameMatcher matcher = shortNameMatchers[0];
        for (int i = 0; i < parts.length; i++) {
            if (!matcher.matchPossible()) {
                return false;
            }
            if (matchesAlternate(parts, i, matcher)) return true;
            matcher = matcher.match(parts[i]);
        }
        return matcher.matches();
    }

    private boolean matches(String[] parts, int index, ShortNameMatcher matcher) {
        for (int i = index; i < parts.length; i++) {
            if (!matcher.matchPossible()) {
                break;
            }
            if (matchesAlternate(parts, i, matcher)) return true;
            matcher = matcher.match(parts[i]);
        }
        return matcher.matches();
    }

    private boolean matchesAlternate(String[] parts, int index, ShortNameMatcher matcher) {
        if (matcher instanceof WildcardShortNameMatcher) {
            ShortNameMatcher alternate = matcher.next().match(parts[index]);
            if (matches(parts, index + 1, alternate)) {
                return true;
            }
        }
        return false;
    }
}
