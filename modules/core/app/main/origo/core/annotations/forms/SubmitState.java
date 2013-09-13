package main.origo.core.annotations.forms;

import com.google.common.collect.Maps;
import main.origo.core.event.NodeContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * The end point for a Submit process. After each \@OnValidation and \@OnSubmit is processed the system
 * uses the \@SubmitState to handle where to go after the process is complete.
 * There can only be one \@SubmitState for each type.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SubmitState {


    String state() default SUCCESS;

    String with();

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    public class Context {
        public final Map<String, Object> args;
        public final Map<String, Object> attributes;

        public Context(Map<String, Object> args) {
            this.args = Maps.newHashMap();
            this.args.putAll(args);
            this.attributes = NodeContext.current().attributes;
        }
    }
}
