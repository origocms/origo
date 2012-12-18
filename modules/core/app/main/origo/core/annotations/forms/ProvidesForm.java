package main.origo.core.annotations.forms;

import main.origo.core.Node;
import main.origo.core.annotations.Interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Interceptor
public @interface ProvidesForm {

    String with();

    public class Context {
        private Node node;
        private String nodeType;
        private Map<String, Object> args;

        public Context(Node node, String nodeType, Map<String, Object> args) {
            this.node = node;
            this.nodeType = nodeType;
            this.args = args;
        }
    }
}
