package org.dessertj.matching;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2023 Hans Jörg Heßmann
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.dessertj.util.Assertions;

/**
 * This pattern identifies a set of classes by their full qualified classname.
 * The syntax has been motivated by the
 * <a href="https://www.eclipse.org/aspectj/doc/released/progguide/quick-typePatterns.html" target="_blank">AspectJ TypeNamePattern</a>
 * with slight modifications:
 * <ul>
 *     <li>The pattern can either be a plain type name, the wildcard *, or an
 *     identifier with embedded * or .. wildcards or the | separator</li>
 *     <li>An * matches any sequence of characters, but does not match the package
 *     separator "."</li>
 *     <li>An | separates alternatives that do not contain a package separator "."</li>
 *     <li>An .. matches any sequence of characters that starts and ends with the package
 *     separator "."</li>
 *     <li>The identifier to match with is always the name returned by {@link Class#getName()}. Thus
 *     $ is the only inner-type separator supported.</li>
 *     <li>The * does match $, too.</li>
 *     <li>A leading .. additionally matches the root package.</li>
 * </ul>
 * Examples:
 * <table summary="Some examples for name patterns">
 *     <thead>
 *         <tr><th>Sample</th><th>Description</th></tr>
 *     </thead>
 *     <tbody>
 *     <tr>
 *         <td>sample.Foo</td><td>Matches only sample.Foo</td>
 *     </tr>
 *     <tr>
 *         <td>sample.Foo*</td><td>Matches all types in sample starting with "Foo" and all inner-types of Foo</td>
 *     </tr>
 *     <tr>
 *         <td>sample.bar|baz.*</td><td>Matches all types in sample.bar and sample.baz</td>
 *     </tr>
 *     <tr>
 *         <td>sample.Foo$*</td><td>Matches only inner-types of Foo</td>
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
 *     </tbody>
 * </table>
 */
public class NamePattern implements Comparable<NamePattern> {
    public static final NamePattern ANY_NAME = new NamePattern(NamePattern.createShortNameMatchers("..*")) {

        @Override
        public boolean matches(String name) {
            return true;
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
        } else if (part.contains("*") || part.contains("|")) {
            return new RegexShortNameMatcher(shortNameMatchers, i, part);
        } else {
            return new ConstantShortNameMatcher(shortNameMatchers, i, part);
        }
    }

    public boolean isAny() {
        return this == ANY_NAME;
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
            if (matchesAlternate(parts, i, matcher)) {
                return true;
            }
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
                return false;
            }
            if (matchesAlternate(parts, i, matcher)) {
                return true;
            }
            matcher = matcher.match(parts[i]);
        }
        return matcher.matches();
    }

    private boolean matchesAlternate(String[] parts, int index, ShortNameMatcher matcher) {
        if (!matcher.isWildcard()) {
            return false;
        }
        ShortNameMatcher alternate = matcher.next();
        for (int i = index; i < parts.length; i++) {
            if (matches(parts, i, alternate)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Use a heuristic to check whether this or the other name pattern should be evaluated first.
     * The name pattern that produces the smallest number of matches is the best to evaluate first,
     * because each match costs additional effort.
     *
     * @param other the other pattern
     * @return true if this pattern should be evaluated first.
     */
    public boolean isMoreConcreteThan(NamePattern other) {
        return weight() > other.weight();
    }

    private int weight() {
        int wildcards = 0;
        for (ShortNameMatcher matcher : shortNameMatchers) {
            if (matcher.isWildcard()) {
                wildcards++;
            }
        }
        return (matcher().isWildcard() ? 0 : 5)
                + shortNameMatchers.length
                - (wildcards > 0 ? 10 : 0);
    }

    /**
     * Make sure name patterns are evaluated in the most useful order
     *
     * @param other the object to be compared.
     * @return -1 if this name pattern is more concrete than the other one
     */
    @Override
    public int compareTo(NamePattern other) {
        int diff = other.weight() - weight();
        if (diff != 0) {
            return diff;
        }
        return toString().compareTo(other.toString());
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
