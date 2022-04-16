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
import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.classfile.MethodInfo;
import de.spricom.dessert.classfile.attribute.Attributes;
import de.spricom.dessert.classfile.attribute.RuntimeVisibleAnnotationsAttribute;
import de.spricom.dessert.slicing.Classpath;
import de.spricom.dessert.slicing.Clazz;
import de.spricom.dessert.util.AnnotationPattern;
import de.spricom.dessert.util.ArrayUtils;
import de.spricom.dessert.util.Predicate;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.Method;

import static org.fest.assertions.Assertions.assertThat;

public class AnnotationsMatcherTest {
    private static final Classpath cp = new Classpath();

    @Test
    public void testCheckMatcher() {
        assertMatches(CheckMatcher.class, AnnotationPattern.of(Retention.class));
        assertMatches(CheckMatcher.class, AnnotationPattern.of(Target.class));
        assertMatches(CheckMatcher.class, AnnotationPattern.of(Retention.class,
                AnnotationPattern.member("value", RetentionPolicy.CLASS)));
        assertMatches(CheckMatcher.class, AnnotationPattern.of(Target.class,
                AnnotationPattern.member("value", ElementType.FIELD, ElementType.TYPE, ElementType.METHOD)));
        assertMatchesNot(CheckMatcher.class, AnnotationPattern.of(Target.class,
                AnnotationPattern.member("value", ElementType.TYPE, ElementType.METHOD)));
        assertMatchesNot(CheckMatcher.class, AnnotationPattern.of(Target.class,
                AnnotationPattern.member("value", ElementType.FIELD, ElementType.TYPE,
                        ElementType.METHOD, ElementType.CONSTRUCTOR)));
    }

    @Test
    @CheckElement(AnnotationsMatcherTest.class)
    public void testCheckElement() {
        assertMatches(getClass(), AnnotationPattern.of(CheckElement.class));
        assertMatches(getClass(), AnnotationPattern.of(CheckElement.class,
                AnnotationPattern.member("value", getClass())));
    }

    @Test(expected = ClassCastException.class)
    public void testCheckElementException() {
        assertMatches(getClass(), AnnotationPattern.of(CheckElement.class,
                AnnotationPattern.member("value", 42)));
    }

    @Test
    @CheckMatcher(bytes = 5)
    public void testCheckMatcherBytes() {
        assertMatches(getClass(), AnnotationPattern.of(CheckMatcher.class));
        assertMatchesNot(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("name", "test")));
        assertMatches(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("bytes", (byte) 5)));
        assertMatches(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("bytes", 5)));
        assertMatches(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("bytes", 1, 2, 3)));
    }

    @Test
    @CheckMatcher(byteValue = 3, charValue = 'B', doubleValue = 7.0, floatValue = 8,
            intValue = 2, longValue = 4, shortValue = 5, booleanValue = true, bytes = {1, 2, 3})
    public void testCheckTypes() {
        assertMatches(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("byteValue", 3)));
        assertMatches(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("charValue", 'B'),
                AnnotationPattern.member("doubleValue", 7.0),
                AnnotationPattern.member("floatValue", 8.0f),
                AnnotationPattern.member("intValue", 2),
                AnnotationPattern.member("longValue", 4L),
                AnnotationPattern.member("shortValue", 5),
                AnnotationPattern.member("booleanValue", true)
        ));
        assertMatchesNot(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("floatValue", 8)));
        assertMatchesNot(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("floatValue", 8.0)));
        assertMatchesNot(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("floatValue", 7.9f)));
        assertMatches(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("floatValue", 8.0f)));
        assertMatches(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("shortValue", (byte) 5)));
        assertMatchesNot(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("shortValue", 5L)));
    }

    @Test
    @CheckMatcher(doubles = {1.1, 2.2, 3.3}, strings = {"A", "B"}, bytes = {})
    public void testCheckArrays() {
        assertMatches(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("doubles", new double[]{1.1, 2.2, 3.3}),
                AnnotationPattern.member("strings", "A", "B"),
                AnnotationPattern.member("bytes", new int[0])
        ));
        assertMatchesNot(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("doubles", new float[]{1.1f, 2.2f, 3.3f})
        ));
        assertMatches(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("doubles", new double[]{3.3, 2.2, 1.1})
        ));
        assertMatchesNot(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("doubles", new double[]{3.3, 2.2f, 1.1})
        ));
        assertMatches(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("bytes", new long[0])
        ));
    }

    @Test
    @CheckMatcher(name = "nesting", element = @CheckElement({String.class, Integer.class}), bytes = 0)
    public void testCheckNesting() {
        assertMatches(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("name", "nesting"),
                AnnotationPattern.member("element", AnnotationPattern.of(CheckElement.class,
                        AnnotationPattern.member("value", Integer.class, String.class))),
                AnnotationPattern.member("bytes", 0)
        ));
        assertMatches(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("element", AnnotationPattern.of(CheckElement.class))));
        assertMatchesNot(getClass(), AnnotationPattern.of(CheckMatcher.class,
                AnnotationPattern.member("element", AnnotationPattern.of(CheckElement.class,
                        AnnotationPattern.member("value", String.class)))
        ));
    }

    private void assertMatches(Class<?> clazz, AnnotationPattern pattern) {
        Clazz cl = cp.asClazz(clazz);
        Predicate<Clazz> predicate = ClazzPredicates.matchesAnnotation(pattern);
        assertThat(predicate.test(cl))
                .as(clazz.getSimpleName() + " matches not " + pattern)
                .isTrue();
    }

    private void assertMatchesNot(Class<?> clazz, AnnotationPattern pattern) {
        Clazz cl = cp.asClazz(clazz);
        Predicate<Clazz> predicate = ClazzPredicates.matchesAnnotation(pattern);
        assertThat(predicate.test(cl))
                .as(clazz.getSimpleName() + " matches " + pattern)
                .isFalse();
    }

    @Test
    @Ignore
    @CheckToString(element = @CheckElement({Short.class, Long.class}),
            shortValue = 5, longValue = 4, strings={"A", "B"}, doubleValue = 7.0,
            doubles={5,6}, floatValue = 8,
            byteValue = 3, intValue = 2, charValue = 'B',
            booleanValue = true,
            bytes = {1, 2, 3}, classValue = String.class)
    public void testToString() throws IOException {
        ClassFile classFile = new ClassFile(this.getClass());
        for (Method method : getClass().getDeclaredMethods()) {
            CheckToString annotation = method.getAnnotation(CheckToString.class);
            if (annotation != null) {
                checkToString(classFile, method, annotation);
            }
        }
    }

    private void checkToString(ClassFile classFile, final Method method, final Annotation annotation) {
        MethodInfo methodInfo = ArrayUtils.findFirst(classFile.getMethods(), new Predicate<MethodInfo>() {
            @Override
            public boolean test(MethodInfo methodInfo) {
                return method.getName().equals(methodInfo.getName());
            }
        });
        RuntimeVisibleAnnotationsAttribute attribute = Attributes.filter(methodInfo.getAttributes(), RuntimeVisibleAnnotationsAttribute.class).get(0);
        de.spricom.dessert.classfile.attribute.Annotation annotationInfo =
                ArrayUtils.findFirst(attribute.getAnnotations(), new Predicate<de.spricom.dessert.classfile.attribute.Annotation>() {
                    @Override
                    public boolean test(de.spricom.dessert.classfile.attribute.Annotation annotationInfo) {
                        return annotation.toString().startsWith("@" + annotationInfo.getType());
                    }
                });
        assertThat(annotationInfo.toString()).isEqualTo(annotation.toString());
    }
}
