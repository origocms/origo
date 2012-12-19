package main.origo.core.helpers.forms;

import main.origo.core.CachedAnnotation;
import main.origo.core.InterceptorRepository;
import main.origo.core.annotations.forms.SubmitHandler;
import main.origo.core.helpers.SettingsHelper;
import org.apache.commons.lang3.StringUtils;
import play.mvc.Result;

import java.util.List;

public class SubmitHandlerHelper {

    public static Class getActiveSubmitHandler() {
        final String postHandlerName = getRegisteredSubmitHandlerName();
        CachedAnnotation cachedAnnotation = getPostHandler(postHandlerName);
        return cachedAnnotation.method.getDeclaringClass();
    }

    public static Result triggerSubmitHandler(String postHandlerName) {
        CachedAnnotation cachedAnnotation = getPostHandler(postHandlerName);
        try {
            return (Result)cachedAnnotation.method.invoke(null, new SubmitHandler.Context());
        } catch (Throwable e) {
            throw new RuntimeException("Unable to invoke method ["+cachedAnnotation.method.toString()+"]", e.getCause());
        }
    }

    // TODO: Cache this instead of looking it up every time
    private static CachedAnnotation getPostHandler(final String postHandler) {
        List<CachedAnnotation> postHandlers = InterceptorRepository.
                getInterceptor(SubmitHandler.class, new CachedAnnotation.InterceptorSelector() {
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
        final String postHandler = SettingsHelper.Core.getSubmitHandler();
        if (StringUtils.isBlank(postHandler)) {
            throw new RuntimeException("No SubmitHandler defined in settings");
        }
        return postHandler;
    }

}
