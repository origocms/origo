package main.origo.core.event.forms;

import main.origo.core.InterceptorRepository;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.internal.CachedAnnotation;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.data.DynamicForm;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class OnSubmitEventGenerator {

    public static void triggerInterceptors(String withType) {
        triggerInterceptors(withType, Collections.<String, Object>emptyMap());
    }

    public static void triggerInterceptors(String withType, Map<String, Object> args) {
        List<CachedAnnotation> cachedAnnotations = findOnPostInterceptorsWithType(withType);
        if (Logger.isDebugEnabled()) {
            StringBuffer sb = new StringBuffer();
            for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
                sb.append(" - ").append(cachedAnnotation.method.getClass()).append("\n");
            }
            Logger.debug("OnSubmitHandler about to be triggered(in order):\n" + sb.toString());
        }
        for (CachedAnnotation cachedAnnotation : cachedAnnotations) {
            try {
                //noinspection unchecked
                cachedAnnotation.method.invoke(null, new OnSubmit.Context(args));
            } catch (Throwable e) {
                Logger.error("", e);
                throw new RuntimeException("Unable to invoke method [" + cachedAnnotation.method.toString() + "]", e.getCause());
            }
        }
    }

    private static List<CachedAnnotation> findOnPostInterceptorsWithType(final String withType) {
        List<CachedAnnotation> onPostInterceptors = InterceptorRepository.getInterceptors(OnSubmit.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation interceptor) {
                OnSubmit annotation = (OnSubmit) interceptor.annotation;
                return StringUtils.isEmpty(annotation.with()) || annotation.with().equals(withType);
            }
        });
        if (onPostInterceptors.isEmpty()) {
            Logger.warn("No @OnSubmit interceptor for with=" + withType + "'");
        }
        Collections.sort(onPostInterceptors, new Comparator<CachedAnnotation>() {
            @Override
            public int compare(CachedAnnotation o1, CachedAnnotation o2) {
                int weight1 = ((OnSubmit) o1.annotation).weight();
                int weight2 = ((OnSubmit) o2.annotation).weight();
                return new Integer(weight1).compareTo(weight2);
            }
        });
        return onPostInterceptors;
    }


}
