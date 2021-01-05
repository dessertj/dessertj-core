package de.spricom.dessert.samples.annotations;

import de.spricom.dessert.samples.annotations.SampleAnnotation;
import de.spricom.dessert.samples.enums.SomeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SampleAnnotation(SomeEnum.VAL3)
public @interface Val3Annotation {
}
