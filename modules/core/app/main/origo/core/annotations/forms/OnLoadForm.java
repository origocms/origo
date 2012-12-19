package main.origo.core.annotations.forms;

import main.origo.core.Node;
import main.origo.core.ui.UIElement;

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

    public static class Context {

        public String withType;
        public Node node;
        public Map<String, Object> args;
        public UIElement formElement;

        public Context(String withType, Node node, Map<String, Object> args) {
            this.withType = withType;
            this.node = node;
            this.args = args;
        }

        public Context(String withType, Node node, Map<String, Object> args, UIElement formElement) {
            this.withType = withType;
            this.node = node;
            this.args = args;
            this.formElement = formElement;
        }

    }
}
