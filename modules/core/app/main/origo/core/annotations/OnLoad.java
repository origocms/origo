package main.origo.core.annotations;

import main.origo.core.Navigation;
import main.origo.core.Node;
import main.origo.core.ui.Element;
import main.origo.core.ui.NavigationElement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.Map;

/**
 * Called before or after a root node is loaded. Modules can use this to modify a root node.
 * <p/>
 * <p/>
 * The method annotated with OnLoad should be a void method and add to or modify the Node passed in as an argument.
 * When type=NODE it is called when a Node is loaded.
 * When type=FORM it is called when a form is created.
 * When type=NAVIGATION is called when the main navigation is loaded.
 * When type=NAVIGATION_ITEM is called for each navigation item being loaded.
 *
 * @see main.origo.core.Node
 * @see main.origo.core.ui.Element
 * @see main.origo.core.ui.NavigationElement
 * @see models.origo.core.navigation.BasicNavigation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OnLoad {

    String type();

    String with() default "";
    int weight() default 1000;
    boolean after() default true;

    public static class Context extends AbstractContext {

        public String withType;

        public Context(Node node, Map<String, Object> args) {
            super(node, args);
            this.withType = null;
        }

        public Context(Node node, String withType, Map<String, Object> args) {
            super(node, args);
            this.withType = withType;
        }

        public Context(Node node, String withType, Navigation navigation) {
            this(node, withType, navigation, Collections.<String, Object>emptyMap());
        }

        public Context(Node node, String withType, Navigation navigation, Map<String, Object> args) {
            super(node, args);
            this.withType = withType;
            this.args.put("navigation", navigation);
        }

        public Context(Node node, String withType, Element element) {
            this(node, withType, element, Collections.<String, Object>emptyMap());
        }

        public Context(Node node, String withType, Element element, Map<String, Object> args) {
            super(node, args);
            this.withType = withType;
            this.args.put("element", element);
        }

        public Context(Node node, String withType, Navigation navigation, NavigationElement element) {
            this(node, withType, navigation, element, Collections.<String, Object>emptyMap());
        }

        public Context(Node node, String withType, Navigation navigation, NavigationElement element, Map<String, Object> args) {
            super(node, args);
            this.withType = withType;
            this.args.put("navigation", navigation);
            this.args.put("navigation.element", element);
        }

    }

}
