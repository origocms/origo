package main.origo.core.annotations.forms;

import com.google.common.collect.Maps;
import main.origo.core.event.NodeContext;

import java.lang.annotation.*;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface OnCreate {

    String with() default "";
    int weight() default 1000;
    boolean after() default true;

    public static class Context {

        public final Object object;
        public final Map<String, Object> args;
        public final Map<String, Object> attributes;

        public Context(Object object, Map<String, Object> args) {
            this.object = object;
            this.args = Maps.newHashMap();
            this.args.putAll(args);
            this.attributes = NodeContext.current().attributes;
        }
    }
}
