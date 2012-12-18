package main.origo.core.annotations;

import main.origo.core.ui.RenderingContext;
import main.origo.core.ui.UIElement;
import models.origo.core.RootNode;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Hooks/Interceptors for each element of the UI rendering process.
 * <p/>
 * The \@Decorates can only be used in a class with \@Theme annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Decorates {

    String type();

    public static class Context {
        public UIElement uiElement;
        public RenderingContext renderingContext;

        public Context(UIElement uiElement, RenderingContext renderingContext) {
            this.uiElement = uiElement;
            this.renderingContext = renderingContext;
        }
    }
}
