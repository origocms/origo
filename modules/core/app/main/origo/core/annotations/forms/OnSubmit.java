package main.origo.core.annotations.forms;

import java.lang.annotation.*;

/**
 * Called when a form is posted. This should be used by all modules altering a form to store the data being posted.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface OnSubmit {

    String with() default "";
    int weight() default 1000;

    Class validate() default Object.class;

}
