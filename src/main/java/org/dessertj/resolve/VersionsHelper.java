package org.dessertj.resolve;

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

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class VersionsHelper {
    private static final Logger log = Logger.getLogger(ClassPackage.class.getName());

    private static final Pattern VERSIONED_ENTRY_PREFIX = Pattern.compile("META-INF/versions/(\\d+)/");

    private VersionsHelper() {}

    public static String removeVersionPrefix(String name) {
        VersionInfo versionInfo = matchVersion(name);
        if (versionInfo != null) {
            return versionInfo.classname;
        }
        return name;
    }

    public static Integer getVersion(String name) {
        try {
            VersionInfo versionInfo = matchVersion(name);
            if (versionInfo != null) {
                return versionInfo.version;
            }
        } catch (NumberFormatException ex) {
            log.warning("Invalid version " + ex.getMessage() + " within " + name);
        }
        return null;
    }

    static VersionInfo matchVersion(String name) {
        Matcher matcher = VERSIONED_ENTRY_PREFIX.matcher(name);
        if (matcher.lookingAt()) {
            return new VersionInfo(name, matcher);
        }
        return null;
    }

    private static Integer parseVersion(String name, Matcher matcher) {
        try {
            return Integer.parseInt(matcher.group(1));
        } catch (NumberFormatException ex) {
            log.warning("Invalid version " + matcher.group(1) + " within " + name);
        }
        return null;
    }

    static class VersionInfo {
        final String classname;
        final Integer version;

        VersionInfo(String name, Matcher matcher) {
            classname = name.substring(matcher.end());
            version = parseVersion(name, matcher);
        }
    }
}
