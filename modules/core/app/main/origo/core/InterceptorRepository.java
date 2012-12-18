package main.origo.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class InterceptorRepository {

    public static Map<Class<? extends Annotation>, List<CachedAnnotation>> interceptor = Maps.newHashMap();

    public static void add(Annotation annotation, Method method) {
        if (!interceptor.containsKey(annotation.annotationType())) {
            interceptor.put(annotation.annotationType(), new CopyOnWriteArrayList<CachedAnnotation>());
        }
        List<CachedAnnotation> annotationTypes = interceptor.get(annotation.annotationType());
        annotationTypes.add(new CachedAnnotation(annotation, method));
    }

    public static List<CachedAnnotation> getInterceptor(Class<? extends Annotation> annotationType) {
        return getInterceptor(annotationType, null);
    }

    public static List<CachedAnnotation> getInterceptor(Class<? extends Annotation> annotationType, CachedAnnotation.InterceptorSelector interceptorSelector) {
        if (interceptor.containsKey(annotationType)) {
            List<CachedAnnotation> interceptorList = interceptor.get(annotationType);
            if (interceptorSelector == null) {
                return interceptorList;
            }
            List<CachedAnnotation> matchingCachedAnnotations = Lists.newArrayList();
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                if (interceptorSelector.isCorrectInterceptor(cachedAnnotation)) {
                    matchingCachedAnnotations.add(cachedAnnotation);
                }
            }
            return matchingCachedAnnotations;
        } else {
            return Collections.emptyList();
        }
    }

    public static void invalidate() {
        interceptor.clear();
    }

    public static Map<Class<? extends Annotation>, List<CachedAnnotation>> getInterceptorMap() {
        return Collections.unmodifiableMap(interceptor);
    }
}
