package com.origocms.core.annotations.forms;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Called when a form is posted. This should be used by all modules altering a form to store the data being posted.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Component
public @interface OnSubmit {

    String with() default "";

}