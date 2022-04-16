package de.spricom.dessert.partitioning;

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
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface CheckMatcher {
    String name() default "test";
    byte[] bytes();
    double[] doubles() default {1.0, 2.0, 3.0};
    String[] strings() default {};
    CheckElement[] element() default {};

    byte byteValue() default 1;
    char charValue() default '0';
    double doubleValue() default 1.0;
    float floatValue() default 2.0f;
    int intValue() default 2;
    long longValue() default 3L;
    short shortValue() default 4;
    boolean booleanValue() default false;
}
