package main.origo.core.annotations;

import main.origo.core.Navigation;
import main.origo.core.Node;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * A method annotated with \@Provides will be called to instantiate a new instance of this type each time
 * a rootNode of this type is loaded from the database.
 * <p/>
 * <p/>
 * A method annotated with \@Provides should return the type it provides as listed below:
 * When type=NODE it adds a new Node to the system and the method should return a Node.
 * When type=FORM it adds a form to edit a Node type and the method should return a UIElement.
 * When type=NAVIGATION it adds a different type of navigation than the standard one and the method should return a NavigationElement.
 * When type=NAVIGATION_ITEM it adds a navigation item to the current navigation type and the method should return a NavigationElement.
 * <p/>
 * Any module can also define a custom type.
 *
 * @see main.origo.core.Node
 * @see main.origo.core.ui.UIElement
 * @see Relationship
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Provides {

    String type();

    String with();

    public static class Context {

        public Node node;
        public Navigation navigation;
        public Map<String, Object> args;

        public Context(Node node, Map<String, Object> args) {
            this.node = node;
            this.args = args;
        }

        public Context(Node node, Navigation navigation, Map<String, Object> args) {
            this.node = node;
            this.navigation = navigation;
            this.args = args;
        }
    }

}
