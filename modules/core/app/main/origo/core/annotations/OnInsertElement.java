package main.origo.core.annotations;

import main.origo.core.Node;
import main.origo.core.event.NodeContext;
import main.origo.core.ui.Element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OnInsertElement {

    Class<? extends Element> with();

    boolean after() default false;

    Class input() default String.class;

    public static class Context {
        public final Node node;
        public final Element parent;
        public final Element element;
        public final Map<String, Object> attributes;

        public Context(Element parent, Element element) {
            this.parent = parent;
            this.element = element;
            this.node = NodeContext.current().node;
            this.attributes = NodeContext.current().attributes;
        }
    }
}