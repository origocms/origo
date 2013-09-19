package main.origo.core.annotations.forms;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface OnDelete {

    String with() default "";
    int weight() default 1000;
    boolean after() default true;

}
