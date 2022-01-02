package de.spricom.dessert.matching;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2022 Hans Jörg Heßmann
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
 * This matcher will be returned after there is a match for each
 * {@link ShortNameMatcher}. Thus at this node the branch matches,
 * hence we have a match this node is a leaf.
 */
enum MatchShortNameMatcher implements ShortNameMatcher {
    MATCH;

    /**
     * Returns a {@link MissShortNameMatcher#MISS}, because if there
     * is something after the match it's no match anymore. Hence
     * the current match was not a leaf.
     */
    @Override
    public ShortNameMatcher match(String shortName) {
        return MissShortNameMatcher.MISS;
    }

    @Override
    public boolean matches() {
        return true;
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
