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

/**
 * This matcher will be returned it the latest consumed short-name does not match after the
 * prefix consumed so far. This matcher terminates the matching process for the current branch.
 */
enum MissShortNameMatcher implements ShortNameMatcher {
    MISS;

    @Override
    public ShortNameMatcher match(String shortName) {
        return MISS;
    }

    @Override
    public boolean matches() {
        return false;
    }

    @Override
    public boolean isLast() {
        return false;
    }

    @Override
    public boolean isMatchPossible() {
        return false;
    }

    @Override
    public boolean isWildcard() {
        return false;
    }

    @Override
    public ShortNameMatcher next() {
        return this;
    }
}
