package main.origo.core.annotations.forms;

import main.origo.core.annotations.Interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Called when a form is posted. This should be used by all modules altering a form to store the data being posted.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Interceptor
public @interface OnSubmit {

    String with() default "";

    public class Context {
        private Map<String, Object> args;

        public Context(Map<String, Object> args) {
            this.args = args;
        }
    }
}
