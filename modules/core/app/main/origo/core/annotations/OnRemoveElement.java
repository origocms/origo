package main.origo.core.annotations;

import main.origo.core.ui.Element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OnRemoveElement {

    String with() default "";

    boolean after() default false;

    public static class Context {
        public Element parent;
        public Element element;

        public Context(Element parent, Element element) {
            this.parent = parent;
            this.element = element;
        }
    }
}
