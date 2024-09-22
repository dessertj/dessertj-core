package org.dessertj.matching;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2024 Hans Jörg Heßmann
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

/**
 * Used for recursive traversal of package trees.
 * It can decide for each branch whether it might be a match.
 * Only if a leaf {@link #matches()} then there is match.
 */
public interface ShortNameMatcher {

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
    boolean isLast();

    /**
     * @return true if the last match was successful, hence the traversal can continue to the next sub-package
     */
    boolean isMatchPossible();

    /**
     * @return true if the last match was done with a ".." wildcard. Hence the alternative where ".." was no
     * match must be examined, too.
     */
    boolean isWildcard();

    /**
     * @return the following matcher to match the next <i>shortName</i>
     */
    ShortNameMatcher next();
}
