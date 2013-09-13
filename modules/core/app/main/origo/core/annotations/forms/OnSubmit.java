package main.origo.core.annotations.forms;

import com.google.common.collect.Maps;
import main.origo.core.event.NodeContext;
import play.data.Form;

import java.lang.annotation.*;
import java.util.Map;

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

    public class Context<T> {

        public final Map<String, Object> args;
        public final Map<String, Object> attributes;
        public final Form<T> form;

        public Context(Map<String, Object> args) {
            this(null, args);
        }

        public Context(Form<T> form, Map<String, Object> args) {
            this.args = Maps.newHashMap();
            this.args.putAll(args);
            this.attributes = NodeContext.current().attributes;
            this.form = form;
        }
    }
}
