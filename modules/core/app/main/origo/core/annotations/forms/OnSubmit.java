package main.origo.core.annotations.forms;

import com.google.common.collect.Maps;
import play.data.DynamicForm;

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
public @interface OnSubmit {

    String with() default "";
    int weight() default 1000;

    public class Context {

        public Map<String, Object> args;

        public Context(Map<String, Object> args) {
            this.args = Maps.newHashMap();
            this.args.putAll(DynamicForm.form().bindFromRequest().data());
            this.args.putAll(args);
        }
    }
}
