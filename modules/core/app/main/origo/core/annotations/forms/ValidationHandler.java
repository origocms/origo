package main.origo.core.annotations.forms;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import main.origo.core.event.NodeContext;
import play.data.Form;
import play.data.validation.ValidationError;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ValidationHandler {

    public class Context {

        public String with;
        public Map<String, Object> attributes;

        public Context(String with) {
            this.with = with;
            this.attributes = NodeContext.current().attributes;
        }

    }

    public class Result {
        public Map<Class, Form> validatedClasses = Maps.newHashMap();

        public Map<String,List<ValidationError>> errors = Maps.newHashMap();
        public List<ValidationError> globalErrors = Lists.newArrayList();

        public boolean hasErrors() {
            return !errors.isEmpty() || !globalErrors.isEmpty();
        }
    }
}
