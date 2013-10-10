package main.origo.core.event.forms;

import com.google.common.collect.Maps;
import main.origo.core.InterceptorRepository;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.forms.Validation;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.internal.ReflectionInvoker;
import play.mvc.Result;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public class ValidationHandlerEventGenerator {

    public static Validation.Result triggerValidationProcessingHandler(String withType) throws ModuleException, NodeLoadException, InvocationTargetException, IllegalAccessException {
        return triggerValidationProcessingHandler(withType, Maps.<String, Object>newHashMap());
    }

    public static Validation.Result triggerValidationProcessingHandler(String withType, Map<String, Object> args) throws ModuleException, NodeLoadException, InvocationTargetException, IllegalAccessException {
        final String postHandlerName = CoreSettingsHelper.getValidationHandler();
        CachedAnnotation cachedAnnotation = getProcessingHandler(postHandlerName, Validation.Processing.class);
        return ReflectionInvoker.execute(cachedAnnotation, withType, args);
    }

    private static CachedAnnotation getProcessingHandler(final String postHandlerName, Class<? extends Annotation> annotationType) {
        Set<CachedAnnotation> postHandlers = InterceptorRepository.
                getInterceptors(annotationType, new CachedAnnotation.InterceptorSelector() {
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

    public static Result triggerValidationFailedHandler(String identifier, String withType, Validation.Result validationResult) throws InvocationTargetException, IllegalAccessException {
        return triggerValidationFailedHandler(identifier, withType, validationResult, Maps.<String, Object>newHashMap());
    }

    public static Result triggerValidationFailedHandler(String identifier, String withType, Validation.Result validationResult, Map<String, Object> args) throws InvocationTargetException, IllegalAccessException {
        CachedAnnotation cachedAnnotation = getFailureHandler(withType, Validation.Failure.class);
        if (cachedAnnotation != null) {
            return ReflectionInvoker.execute(cachedAnnotation, identifier, withType, validationResult, args);
        } else {
            cachedAnnotation = getFailureHandler(Core.With.VALIDATION_DEFAULT_FAILURE, Validation.Failure.class);
            return ReflectionInvoker.execute(cachedAnnotation, identifier, Core.With.VALIDATION_DEFAULT_FAILURE, validationResult, args);
        }
    }

    private static CachedAnnotation getFailureHandler(final String withType, Class<? extends Annotation> annotationType) {
        Set<CachedAnnotation> postHandlers = InterceptorRepository.
                getInterceptors(annotationType, new CachedAnnotation.InterceptorSelector() {
                    @Override
                    public boolean isCorrectInterceptor(CachedAnnotation listener) {
                        return withType.equals(((Validation.Failure)listener.annotation).with());
                    }
                });

        if (postHandlers.isEmpty()) {
            throw new RuntimeException("No ValidationHandler found");
        }
        return postHandlers.iterator().next();
    }

}
