package main.origo.core.event.forms;

import main.origo.core.InterceptorRepository;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.forms.SubmitHandler;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.internal.CachedAnnotation;
import org.apache.commons.lang3.StringUtils;
import play.mvc.Result;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

public class SubmitHandlerEventGenerator {

    public static Class getActiveSubmitHandler() {
        final String postHandlerName = getRegisteredSubmitHandlerName();
        CachedAnnotation cachedAnnotation = getPostHandler(postHandlerName);
        return cachedAnnotation.method.getDeclaringClass();
    }

    public static Result triggerSubmitHandler(String postHandlerName) throws ModuleException, NodeLoadException {
        CachedAnnotation cachedAnnotation = getPostHandler(postHandlerName);
        try {
            return (Result) cachedAnnotation.method.invoke(null, new SubmitHandler.Context());
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof ModuleException) {
                throw (ModuleException) e.getCause();
            } else if (e.getCause() instanceof NodeLoadException) {
                throw (NodeLoadException) e.getCause();
            } else {
                throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.method.toString() + "]", e.getCause());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.method.toString() + "]", e.getCause());
        }
    }

    // TODO: Cache this instead of looking it up every time
    private static CachedAnnotation getPostHandler(final String postHandler) {
        Set<CachedAnnotation> postHandlers = InterceptorRepository.
                getInterceptors(SubmitHandler.class, new CachedAnnotation.InterceptorSelector() {
                    @Override
                    public boolean isCorrectInterceptor(CachedAnnotation listener) {
                        return postHandler.equals(listener.method.getDeclaringClass().getName());
                    }
                });

        if (postHandlers.isEmpty()) {
            throw new RuntimeException("No SubmitHandler found");
        }
        return postHandlers.iterator().next();
    }

    public static String getRegisteredSubmitHandlerName() {
        final String postHandler = CoreSettingsHelper.getSubmitHandler();
        if (StringUtils.isBlank(postHandler)) {
            throw new RuntimeException("No SubmitHandler defined in settings");
        }
        return postHandler;
    }

}
