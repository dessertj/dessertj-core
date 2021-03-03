package de.spricom.dessert.assertions;

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

import de.spricom.dessert.slicing.Slice;

import java.util.Arrays;
import java.util.Map;

/**
 * This class is the starting point for any dessert assertion. An example of a typical
 * dessert test is:
 * <pre>
 *      Classpath cp = new Classpath();
 *      Clazz me = cp.asClazz(this.getClass());
 *      Root root = cp.rootOf(Test.class);
 *      SliceAssertions.dessert(me).usesNot(root);
 * </pre>
 */
public final class SliceAssertions {
    private SliceAssertions() {
    }

    /**
     * Starts an assertion for slices passed. The result provides a fluent API to specfiy the
     * actual assertions. The method name <i>dessert</i> is a short form for
     * <i>dependency assert that</i>.
     *
     * @param slices the slices to verify the assertion for
     * @return an {@link SliceAssert} object that provides a fluent api to specify the assertions
     */
    public static SliceAssert dessert(Iterable<? extends Slice> slices) {
        return new SliceAssert(slices);
    }

    /**
     * An convenience method to specify slices for assertions.
     *
     * @param slices the slices to verify the assertion for
     * @return an {@link SliceAssert} object that provides a fluent api to specify the assertions
     */
    public static SliceAssert dessert(Slice... slices) {
        return dessert(Arrays.asList(slices));
    }

    /**
     * An other convenience method to specify slices for assertions.
     *
     * @param slices the slices to verify the assertion for
     * @return an {@link SliceAssert} object that provides a fluent api to specify the assertions
     */
    public static SliceAssert dessert(Map<String, ? extends Slice> slices) {
        return dessert(slices.values());
    }
}
