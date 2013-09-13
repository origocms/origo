package main.origo.core.annotations.forms;

import main.origo.core.event.NodeContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SubmitHandler {

    public class Context {

        public final Map<String, Object> attributes;

        public Context() {
            this.attributes = NodeContext.current().attributes;
        }

    }
}
