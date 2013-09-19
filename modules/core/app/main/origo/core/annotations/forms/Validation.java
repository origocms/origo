package main.origo.core.annotations.forms;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public @interface Failure {

        String with();

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
