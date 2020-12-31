package de.spricom.dessert.classfile.samples;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SampleAnnotation {
    /**
     * Some samples value.
     */
    public SomeEnum value();
}
