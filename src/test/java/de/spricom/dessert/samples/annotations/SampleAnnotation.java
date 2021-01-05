package de.spricom.dessert.samples.annotations;

import de.spricom.dessert.samples.enums.SomeEnum;

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
