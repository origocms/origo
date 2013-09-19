package main.origo.core.event.forms;

import com.google.common.collect.Maps;
import controllers.origo.core.CoreLoader;
import main.origo.core.InterceptorRepository;
import main.origo.core.annotations.forms.SubmitState;
import main.origo.core.event.EventGeneratorUtils;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.internal.ReflectionInvoker;
import play.Logger;
import play.mvc.Result;

import java.util.Map;
import java.util.Set;

public class SubmitStateEventGenerator {

    public static Result triggerInterceptor(String state, String withType) {
        return triggerInterceptor(state, withType, Maps.<String, Object>newHashMap());
    }

    public static Result triggerInterceptor(String state, String withType, Map<String, Object> args) {
        CachedAnnotation cachedAnnotation = findOnPostInterceptorsWithType(state, withType);
        switch(state) {
            case SubmitState.SUCCESS:
                if (cachedAnnotation == null) {
                    throw new RuntimeException("Every form type (specified by using attribute 'with') must have a class annotated with @SubmitState to use as an endpoint for submits. Unable to find a SubmitState for state='" + state + "' and type='" + withType + "'");
                }
                return ReflectionInvoker.execute(cachedAnnotation, args);
            case SubmitState.FAILURE:
                if (cachedAnnotation == null) {
                    Logger.error("No @SubmitState found to use as an endpoint for submit's. Unable to find a SubmitState for state='" + state + "' and type='" + withType + "'");
                    return CoreLoader.redirectToStartPage();
                }
                return ReflectionInvoker.execute(cachedAnnotation, args);
            default:
                Logger.error("Unknown state '"+state+"' for SubmitState, showing error page");
                return CoreLoader.redirectToPageLoadErrorPage();
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
