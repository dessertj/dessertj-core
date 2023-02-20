package de.spricom.dessert.matching;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
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

abstract class AbstractShortNameMatcher implements ShortNameMatcher {

    private final ShortNameMatcher[] shortNameMatchers;
    private final int index;

    protected AbstractShortNameMatcher(ShortNameMatcher[] shortNameMatchers, int index) {
        this.shortNameMatchers = shortNameMatchers;
        this.index = index;
    }

    public ShortNameMatcher next() {
        int next = index + 1;
        if (next == shortNameMatchers.length) {
            // completely matched => matches() == true
            return MatchShortNameMatcher.MATCH;
        }
        return shortNameMatchers[next];
    }

    @Override
    public boolean isWildcard() {
        return false;
    }

    @Override
    public boolean isLast() {
        return index + 1 == shortNameMatchers.length;
    }

    @Override
    public boolean isMatchPossible() {
        return true;
    }

    @Override
    public boolean matches() {
        return false;
    }
}
