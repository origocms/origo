package main.origo.core.annotations;

import main.origo.core.Navigation;
import main.origo.core.Node;
import main.origo.core.ui.NavigationElement;
import main.origo.core.ui.UIElement;
import models.origo.core.RootNode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
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
 * @see main.origo.core.ui.UIElement
 * @see main.origo.core.ui.NavigationElement
 * @see models.origo.core.navigation.BasicNavigation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OnLoad {

    String type();

    String with() default "";

    boolean after() default true;

    public static class Context {
        public RootNode rootNode;
        public Node node;
        public Map<String, Object> args;
        public Navigation navigation;
        public NavigationElement navigationElement;
        public List<NavigationElement> navigationElements;
        public UIElement uiElement;

        public Context(RootNode rootNode, Map<String, Object> args) {
            this.rootNode = rootNode;
            this.args = args;
        }

        public Context(Node node, Map<String, Object> args) {
            this.node = node;
            this.args = args;
        }

        public Context(Node node, Navigation navigation, Map<String, Object> args) {
            this.node = node;
            this.navigation = navigation;
            this.args = args;
        }

        public Context(Node node, List<NavigationElement> navigationElements, Map<String, Object> args) {
            this.node = node;
            this.navigationElements = navigationElements;
            this.args = args;
        }

        public Context(Node node, UIElement uiElement, Map<String, Object> args) {
            this.node = node;
            this.uiElement = uiElement;
            this.args = args;
        }

        public Context(Node node, Navigation navigation, NavigationElement navigationElement, Map<String, Object> args) {
            this.node = node;
            this.navigation = navigation;
            this.navigationElement = navigationElement;
            this.args = args;
        }
    }

}
