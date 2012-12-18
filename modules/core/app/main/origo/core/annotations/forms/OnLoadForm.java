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
public @interface OnLoadForm {

    String with() default "";

    boolean after() default true;

    public static class Context {

        public Node node;
        public String nodeType;
        public Map<String, Object> args;

        public Context(Node node, String nodeType, Map<String, Object> args) {
            this.node = node;
            this.nodeType = nodeType;
            this.args = args;
        }

        /*
        public UIElement uiElement;

        public Context(Node node, UIElement uiElement, Map<String, Object> args) {
            this.node = node;
            this.uiElement = uiElement;
            this.args = args;
        }
*/
    }
}
