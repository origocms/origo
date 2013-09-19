package main.origo.core.annotations;

import main.origo.core.ui.Element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OnRemoveElement {

    Class<? extends Element> with();

    boolean after() default false;
    int weight() default 1000;
    Class input() default String.class;

}
