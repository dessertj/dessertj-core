package org.dessertj.classfile.attribute;

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

import java.util.LinkedList;
import java.util.List;

/**
 * Utility methods to access attributes.
 */
public final class Attributes {

    private Attributes() {
    }

    public static <A extends AttributeInfo> List<A> filter(AttributeInfo[] attributes, Class<A> attributeClass) {
        String attributeName = attributeName(attributeClass);
        List<A> matches = new LinkedList<A>();
        for (AttributeInfo attribute : attributes) {
            if (attributeName.equals(attribute.getName())) {
                matches.add((A) attribute);
            }
        }
        return matches;
    }

    public static String attributeName(Class<? extends AttributeInfo> attributeClass) {
        String className = attributeClass.getSimpleName();
        String attributeName = className.substring(0, className.length() - "Attribute".length());
        return attributeName;
    }
}
