package main.origo.core.annotations;

import main.origo.core.Navigation;
import main.origo.core.Node;
import main.origo.core.ui.Element;
import main.origo.core.ui.NavigationElement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
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

    public static interface Context {

        public Node node();
        public String withType();
        public Map<String, Object> args();
        public Map<String, Object> attributes();

        public static class NodeContext extends AbstractContext implements Context {
            private final String withType;

            public NodeContext(Node node, Map<String, Object> args) {
                super(node, args);
                this.withType = null;
            }

            public NodeContext(Node node, String withType, Map<String, Object> args) {
                super(node, args);
                this.withType = withType;
            }

            public String withType() {
                return withType;
            }
        }

        public static class NavigationContext extends NodeContext {
            private final Navigation navigation;

            public NavigationContext(Node node, String withType, Navigation navigation, Map<String, Object> args) {
                super(node, withType, args);
                this.navigation = navigation;
            }

            public Navigation navigation() {
                return navigation;
            }
        }

/*
        public static class NavigationElementsContext extends NodeContext {
            private List<NavigationElement> navigationElements;

            public NavigationElementsContext(Node node, String withType, List<NavigationElement> navigationElements, Map<String, Object> args) {
                super(node, withType, args);
                this.navigationElements = navigationElements;
            }
        }
*/

        public static class ElementContext extends NodeContext {
            private Element element;

            public ElementContext(Node node, String withType, Element element, Map<String, Object> args) {
                super(node, withType, args);
                this.element = element;
            }

            public Element element() {
                return element;
            }
        }

        public static class NavigationElementContext extends NavigationContext {
            private final NavigationElement navigationElement;

            public NavigationElementContext(Node node, String withType, Navigation navigation, NavigationElement navigationElement, Map<String, Object> args) {
                super(node, withType, navigation, args);
                this.navigationElement = navigationElement;
            }

            public NavigationElement element() {
                return navigationElement;
            }
        }
    }

}
