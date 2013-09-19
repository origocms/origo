package main.origo.core.event.forms;

import controllers.origo.core.CoreLoader;
import main.origo.core.InterceptorRepository;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.forms.SubmitHandler;
import main.origo.core.internal.CachedAnnotation;
import play.mvc.Result;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class SubmitHandlerEventGenerator {

    public static Result triggerSubmitHandler(String postHandlerName) throws ModuleException, NodeLoadException {
        CachedAnnotation cachedAnnotation = getPostHandler(postHandlerName);
        try {
            return (Result) cachedAnnotation.method.invoke(null);
        } catch (InvocationTargetException e) {
            return CoreLoader.handleException(e.getCause());
        } catch (IllegalAccessException e) {
            return CoreLoader.handleException(e);
        }
    }

    private static CachedAnnotation getPostHandler(final String postHandlerName) {
        Set<CachedAnnotation> postHandlers = InterceptorRepository.
                getInterceptors(SubmitHandler.class, new CachedAnnotation.InterceptorSelector() {
                    @Override
                    public boolean isCorrectInterceptor(CachedAnnotation listener) {
                        return postHandlerName.equals(listener.method.getDeclaringClass().getName());
                    }
                });

        if (postHandlers.isEmpty()) {
            throw new RuntimeException("No SubmitHandler found");
        }
        return postHandlers.iterator().next();
    }

}
