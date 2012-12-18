package main.origo.core.helpers.forms;

import main.origo.core.CachedAnnotation;
import main.origo.core.InterceptorRepository;
import main.origo.core.annotations.forms.SubmitState;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmitStateHelper {

    public static void triggerInterceptor(String state, String withType) {
        triggerInterceptor(state, withType, Collections.<String, Object>emptyMap());
    }

    public static void triggerInterceptor(String state, String withType, Map<String, Object> args) {
        triggerInterceptor(state, withType, new SubmitState.Context(args));
    }

    public static void triggerInterceptor(String state, String withType, SubmitState.Context context) {
        CachedAnnotation cachedAnnotation = findOnPostInterceptorsWithType(state, withType);
        try {
            cachedAnnotation.method.invoke(null, context);
        } catch (Throwable e) {
            throw new RuntimeException("", e);
        }
    }

    private static CachedAnnotation findOnPostInterceptorsWithType(final String state, final String withType) {
        List<CachedAnnotation> submitStateInterceptors = InterceptorRepository.getInterceptor(SubmitState.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation interceptor) {
                SubmitState annotation = (SubmitState) interceptor.annotation;
                return annotation.state().equals(state) && annotation.with().equals(withType);
            }
        });
        if (submitStateInterceptors.isEmpty()) {
            throw new RuntimeException("Every form type (specified by using attribute 'with') must have a class annotated with @SubmitState to use as an endpoint for submit\'s. Unable to find a SubmitState for state=\'" + state + "\' and type=\'" + withType + "\'");
        }
        if (submitStateInterceptors.size() > 1) {
            throw new RuntimeException("Only one @SubmitState(state=\'" + state + "\') per type (attribute 'with') is allowed");
        }
        return submitStateInterceptors.iterator().next();
    }

}
