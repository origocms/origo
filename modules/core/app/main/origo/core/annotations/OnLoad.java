package main.origo.core.annotations;

import main.origo.core.Navigation;
import main.origo.core.Node;
import main.origo.core.event.NodeContext;
import main.origo.core.ui.Element;
import main.origo.core.ui.NavigationElement;
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
 * @see main.origo.core.ui.Element
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
        public final RootNode rootNode;
        public final Node node;
        public final Map<String, Object> args;
        public final Navigation navigation;
        public final NavigationElement navigationElement;
        public final List<NavigationElement> navigationElements;
        public final Element element;
        public final Map<String, Object> attributes;

        public Context(RootNode rootNode, Map<String, Object> args) {
            this.rootNode = rootNode;
            this.args = args;
            this.attributes = NodeContext.current().attributes;
            navigation = null;
            element = null;
            navigationElements = null;
            navigationElement = null;
            node = null;
        }

        public Context(Node node, Map<String, Object> args) {
            this.node = node;
            this.args = args;
            this.attributes = NodeContext.current().attributes;
            navigation = null;
            element = null;
            navigationElements = null;
            navigationElement = null;
            rootNode = null;
        }

        public Context(Node node, Navigation navigation, Map<String, Object> args) {
            this.node = node;
            this.navigation = navigation;
            this.args = args;
            this.attributes = NodeContext.current().attributes;
            element = null;
            navigationElements = null;
            navigationElement = null;
            rootNode = null;
        }

        public Context(Node node, List<NavigationElement> navigationElements, Map<String, Object> args) {
            this.node = node;
            this.navigationElements = navigationElements;
            this.args = args;
            this.attributes = NodeContext.current().attributes;
            navigation = null;
            element = null;
            navigationElement = null;
            rootNode = null;
        }

        public Context(Node node, Element element, Map<String, Object> args) {
            this.node = node;
            this.element = element;
            this.args = args;
            this.attributes = NodeContext.current().attributes;
            navigation = null;
            navigationElements = null;
            navigationElement = null;
            rootNode = null;
        }

        public Context(Node node, Navigation navigation, NavigationElement navigationElement, Map<String, Object> args) {
            this.node = node;
            this.navigation = navigation;
            this.navigationElement = navigationElement;
            this.args = args;
            this.attributes = NodeContext.current().attributes;
            element = null;
            navigationElements = null;
            rootNode = null;
        }
    }

}
