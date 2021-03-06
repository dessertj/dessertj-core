package de.spricom.dessert.samples;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2021 Hans Jörg Heßmann
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

import de.spricom.dessert.slicing.Classpath;
import de.spricom.dessert.slicing.Slice;
import org.junit.Test;

import static de.spricom.dessert.assertions.SliceAssertions.dessert;

public class SampleTest {
    private static final Classpath cp = new Classpath();

    @Test
    public void testDetectUsageOfJdkInternalApis() {
        Slice myCompanyCode = cp.slice("de.spricom..*");
        Slice jdkInternalApis = cp.slice("sun..*").plus(cp.slice("com.sun..*"));
        dessert(myCompanyCode).usesNot(jdkInternalApis);
    }
}
