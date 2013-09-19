package main.origo.core.annotations;

import main.origo.core.ui.Element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Hooks/Interceptors for each element of the UI rendering process.
 * <p/>
 * The \@Decorates can only be used in a class with \@Theme annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Decorates {

    Class<? extends Element>[] types();

    Class input() default String.class;

}
