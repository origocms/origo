package main.origo.core.annotations.forms;

import main.origo.core.annotations.Interceptor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Interceptor
public @interface SubmitHandler {

    public class Context {
    }
}
