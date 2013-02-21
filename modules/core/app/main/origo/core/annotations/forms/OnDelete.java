package main.origo.core.annotations.forms;

import com.google.common.collect.Maps;
import main.origo.core.Navigation;
import main.origo.core.Node;
import play.data.DynamicForm;

import java.lang.annotation.*;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface OnDelete {

    String with() default "";
    int weight() default 1000;
    boolean after() default true;

    public static interface Context {

        public static class NodeContext implements Context {

            public Node node;
            public Map<String, Object> args;

            public NodeContext(Node node, Map<String, Object> args) {
                this.node = node;
                this.args = Maps.newHashMap();
                this.args.putAll(DynamicForm.form().bindFromRequest().data());
                this.args.putAll(args);
            }
        }

        public static class NavigationContext implements Context {

            public Navigation navigation;
            public Map<String, Object> args;

            public NavigationContext(Navigation navigation, Map<String, Object> args) {
                this.navigation = navigation;
                this.args = Maps.newHashMap();
                this.args.putAll(DynamicForm.form().bindFromRequest().data());
                this.args.putAll(args);
            }
        }

        public static class DefaultContext implements Context {

            public Object object;
            public Map<String, Object> args;

            public DefaultContext(Object object, Map<String, Object> args) {
                this.object = object;
                this.args = args;
            }
        }
    }
}
