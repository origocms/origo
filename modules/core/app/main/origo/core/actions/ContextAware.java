package main.origo.core.actions;

import main.origo.core.event.NodeContext;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@With(ContextAware.ContextAction.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContextAware {

    public static class ContextAction extends Action.Simple {
        @Override
        public Result call(Http.Context context) throws Throwable {
            try {
                NodeContext.set();
                return delegate.call(context);
            } finally {
                NodeContext.clear();
            }
        }

    }
}
