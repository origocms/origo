package main.origo.core.annotations.forms;

import main.origo.core.Node;
import main.origo.core.annotations.AbstractContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ProvidesForm {

    String with();

    public class Context extends AbstractContext {

        public Context(Node node, Map<String, Object> args) {
            super(node, args);
        }
    }
}
