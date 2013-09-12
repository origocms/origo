package main.origo.core.event.forms;

import main.origo.core.InterceptorRepository;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.forms.ValidationHandler;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.internal.CachedAnnotation;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class ValidationHandlerEventGenerator {

    public static Class getActiveValidationHandler() {
        final String postHandlerName = getRegisteredValidationHandlerName();
        CachedAnnotation cachedAnnotation = getPostHandler(postHandlerName);
        return cachedAnnotation.method.getDeclaringClass();
    }

    public static ValidationHandler.Result triggerValidationHandler(String postHandlerName, String withType) throws ModuleException, NodeLoadException, InvocationTargetException, IllegalAccessException {
        CachedAnnotation cachedAnnotation = getPostHandler(postHandlerName);
        return (ValidationHandler.Result)cachedAnnotation.method.invoke(null, new ValidationHandler.Context(withType));
    }

    private static CachedAnnotation getPostHandler(final String postHandlerName) {
        Set<CachedAnnotation> postHandlers = InterceptorRepository.
                getInterceptors(ValidationHandler.class, new CachedAnnotation.InterceptorSelector() {
                    @Override
                    public boolean isCorrectInterceptor(CachedAnnotation listener) {
                        return postHandlerName.equals(listener.method.getDeclaringClass().getName());
                    }
                });

        if (postHandlers.isEmpty()) {
            throw new RuntimeException("No ValidationHandler found");
        }
        return postHandlers.iterator().next();
    }

    public static String getRegisteredValidationHandlerName() {
        final String postHandler = CoreSettingsHelper.getValidationHandler();
        if (StringUtils.isBlank(postHandler)) {
            throw new RuntimeException("No SubmitHandler defined in settings");
        }
        return postHandler;
    }

}
