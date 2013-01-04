package main.origo.core.annotations.forms;

import main.origo.core.Node;
import main.origo.core.annotations.AbstractContext;
import main.origo.core.ui.Element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OnLoadForm {

    String with() default "";

    boolean after() default true;

    public static class Context extends AbstractContext {

        public String withType;
        public Element formElement;

        public Context(Node node, String withType, Map<String, Object> args) {
            super(node, args);
            this.withType = withType;
        }

        public Context(Node node, String withType, Map<String, Object> args, Element formElement) {
            super(node, args);
            this.withType = withType;
            this.formElement = formElement;
        }

    }
}
