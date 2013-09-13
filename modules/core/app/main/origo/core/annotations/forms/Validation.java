package main.origo.core.annotations.forms;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import main.origo.core.Node;
import main.origo.core.event.NodeContext;
import play.data.Form;
import play.data.validation.ValidationError;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

public class Validation {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public @interface Processing {

        public class Context {

            public final String with;
            public final Map<String, Object> attributes;
            public final Map<String, Object> args;

            public Context(String with, Map<String, Object> args) {
                this.with = with;
                this.attributes = NodeContext.current().attributes;
                this.args = Maps.newHashMap();
                this.args.putAll(args);
            }

        }

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public @interface Failure {

        String with();

        public class Context {

            public final Node node;
            public final String with;
            public final Map<String, Object> attributes;
            public final Map<String, Object> args;
            public final Result result;

            public Context(Node node, String with, Map<String, Object> args) {
                this(node, with, new Result(), args);
            }

            public Context(Node node, String with, Result result, Map<String, Object> args) {
                this.node = node;
                this.with = with;
                this.attributes = NodeContext.current().attributes;
                this.args = Maps.newHashMap();
                this.args.putAll(args);
                this.result = result;
            }

        }

    }

    public static class Result {

        public Map<Class, Form> validatedClasses = Maps.newHashMap();

        public Map<String,List<ValidationError>> errors = Maps.newHashMap();
        public List<ValidationError> globalErrors = Lists.newArrayList();

        public boolean hasErrors() {
            return !errors.isEmpty() || !globalErrors.isEmpty();
        }
    }
}
