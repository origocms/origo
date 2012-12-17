package main.origo.core.helpers.forms;

import main.origo.core.CachedAnnotation;
import main.origo.core.InterceptorRepository;
import main.origo.core.annotations.forms.OnSubmit;
import play.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OnSubmitHelper {

    public static void triggerInterceptors(String withType) {
        triggerInterceptors(withType, Collections.<String, Object>emptyMap());
    }

    public static void triggerInterceptors(String withType, Map<String, Object> args) {
        List<CachedAnnotation> cachedAnnotations = findOnPostInterceptorsWithType(withType);
        try {
            for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
                //noinspection unchecked
                cachedAnnotation.method.invoke(null, new OnSubmit.Context(args));
            }
        } catch (Throwable e) {
            Logger.error("", e);
            throw new RuntimeException("Unable to invoke method", e.getCause());
        }
    }

    private static List<CachedAnnotation> findOnPostInterceptorsWithType(final String withType) {
        List<CachedAnnotation> onPostInterceptors = InterceptorRepository.getInterceptor(OnSubmit.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation interceptor) {
                OnSubmit annotation = (OnSubmit) interceptor.annotation;
                return annotation.with().equals(withType);
            }
        });
        if (onPostInterceptors.isEmpty()) {
            Logger.warn("No @OnSubmit interceptor for with=\'" + withType + "\'");
        }
        return onPostInterceptors;
    }


}
