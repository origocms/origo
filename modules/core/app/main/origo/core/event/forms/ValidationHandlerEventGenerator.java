package main.origo.core.event.forms;

import com.google.common.collect.Maps;
import main.origo.core.InterceptorRepository;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.forms.Validation;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.internal.CachedAnnotation;

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
        return (Validation.Result)cachedAnnotation.method.invoke(null, new Validation.Processing.Context(withType, args));
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

    public static Node triggerValidationFailedHandler(Node node, String withType, Validation.Result validationResult) throws InvocationTargetException, IllegalAccessException {
        return triggerValidationFailedHandler(node, withType, validationResult, Maps.<String, Object>newHashMap());
    }

    public static Node triggerValidationFailedHandler(Node node, String withType, Validation.Result validationResult, Map<String, Object> args) throws InvocationTargetException, IllegalAccessException {
        CachedAnnotation cachedAnnotation = getFailureHandler(withType, Validation.Failure.class);
        return (Node)cachedAnnotation.method.invoke(null, new Validation.Failure.Context(node, withType, validationResult, args));
    }

    private static CachedAnnotation getFailureHandler(final String withType, Class<? extends Annotation> annotationType) {
        Set<CachedAnnotation> postHandlers = InterceptorRepository.
                getInterceptors(annotationType, new CachedAnnotation.InterceptorSelector() {
                    @Override
                    public boolean isCorrectInterceptor(CachedAnnotation listener) {
                        return withType.equals(((Validation.Failure)listener).with());
                    }
                });

        if (postHandlers.isEmpty()) {
            throw new RuntimeException("No ValidationHandler found");
        }
        return postHandlers.iterator().next();
    }

}
