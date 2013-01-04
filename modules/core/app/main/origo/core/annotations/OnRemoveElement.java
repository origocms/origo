package main.origo.core.annotations;

import com.google.common.collect.Maps;
import main.origo.core.Node;
import main.origo.core.ui.Element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OnRemoveElement {

    Class<? extends Element> with();

    boolean after() default false;

    Class input() default String.class;

    public static class Context extends AbstractContext {
        public final Element parent;
        public final Element element;

        public Context(Node node, Element parent, Element element) {
            super(node, Maps.<String, Object>newHashMap());
            this.parent = parent;
            this.element = element;
        }
    }
}
