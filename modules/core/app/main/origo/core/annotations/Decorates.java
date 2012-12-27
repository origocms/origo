package main.origo.core.annotations;

import main.origo.core.event.NodeContext;
import main.origo.core.ui.Element;
import main.origo.core.ui.RenderingContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Hooks/Interceptors for each element of the UI rendering process.
 * <p/>
 * The \@Decorates can only be used in a class with \@Theme annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Decorates {

    Class<? extends Element>[] types();

    Class input() default String.class;

    public static class Context {
        public final Element element;
        public final RenderingContext renderingContext;
        public final Map<String, Object> attributes;

        public Context(Element element, RenderingContext renderingContext) {
            this.element = element;
            this.renderingContext = renderingContext;
            this.attributes = NodeContext.current().attributes;
        }


    }
}
