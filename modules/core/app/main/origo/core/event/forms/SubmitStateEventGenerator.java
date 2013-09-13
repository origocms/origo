package main.origo.core.event.forms;

import controllers.origo.core.CoreLoader;
import main.origo.core.InterceptorRepository;
import main.origo.core.annotations.forms.SubmitState;
import main.origo.core.annotations.forms.ValidationHandler;
import main.origo.core.event.EventGeneratorUtils;
import main.origo.core.internal.CachedAnnotation;
import play.Logger;
import play.mvc.Result;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class SubmitStateEventGenerator {

    public static Result triggerInterceptor(String state, String withType) {
        return triggerInterceptor(state, withType, new ValidationHandler.Result(), Collections.<String, Object>emptyMap());
    }

    public static Result triggerInterceptor(String state, String withType, ValidationHandler.Result validationResult) {
        return triggerInterceptor(state, withType, validationResult, Collections.<String, Object>emptyMap());
    }

    public static Result triggerInterceptor(String state, String withType, Map<String, Object> args) {
        return triggerInterceptor(state, withType, new ValidationHandler.Result(), new SubmitState.Context(args));
    }

    public static Result triggerInterceptor(String state, String withType, ValidationHandler.Result validationResult, Map<String, Object> args) {
        return triggerInterceptor(state, withType, validationResult, new SubmitState.Context(args));
    }

    public static Result triggerInterceptor(String state, String withType, ValidationHandler.Result validationResult, SubmitState.Context context) {
        CachedAnnotation cachedAnnotation = findOnPostInterceptorsWithType(state, withType);
        context.validationResult = validationResult;
        try {
            switch(state) {
                case SubmitState.SUCCESS:
                    if (cachedAnnotation == null) {
                        throw new RuntimeException("Every form type (specified by using attribute 'with') must have a class annotated with @SubmitState to use as an endpoint for submit's. Unable to find a SubmitState for state='" + state + "' and type='" + withType + "'");
                    }
                    return (Result) cachedAnnotation.method.invoke(null, context);
                case SubmitState.FAILURE:
                case SubmitState.VALIDATION:
                    if (cachedAnnotation == null) {
                        Logger.error("No @SubmitState found to use as an endpoint for submit's. Unable to find a SubmitState for state='" + state + "' and type='" + withType + "'");
                        return CoreLoader.loadStartPage();
                    }
                    return (Result) cachedAnnotation.method.invoke(null, context);
                default:
                    Logger.error("Unknown state '"+state+"' for SubmitState, showing error page");
                    return CoreLoader.redirectToPageLoadErrorPage();
            }
        } catch (Throwable e) {
            throw new RuntimeException("", e);
        }
    }

    private static CachedAnnotation findOnPostInterceptorsWithType(final String state, final String withType) {
        Set<CachedAnnotation> submitStateInterceptors = InterceptorRepository.getInterceptors(SubmitState.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation interceptor) {
                SubmitState annotation = (SubmitState) interceptor.annotation;
                return annotation.state().equals(state) && annotation.with().equals(withType);
            }
        });
        return EventGeneratorUtils.selectEventHandler(SubmitState.class, state, withType, submitStateInterceptors);
    }

}
