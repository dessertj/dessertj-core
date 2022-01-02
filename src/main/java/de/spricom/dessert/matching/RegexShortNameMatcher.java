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
