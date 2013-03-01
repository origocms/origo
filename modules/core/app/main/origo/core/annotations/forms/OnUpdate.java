package main.origo.core.annotations.forms;

import com.google.common.collect.Maps;
import play.data.DynamicForm;

import java.lang.annotation.*;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface OnUpdate {

    String with() default "";
    int weight() default 1000;
    boolean after() default true;

    public static class Context {

        public Object object;
        public Map<String, Object> args;

        public Context(Object object, Map<String, Object> args) {
            this.object = object;
            this.args = Maps.newHashMap();
            this.args.putAll(DynamicForm.form().bindFromRequest().data());
            this.args.putAll(args);
        }
    }
}
