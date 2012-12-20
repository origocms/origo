package main.origo.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import main.origo.core.annotations.Relationship;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class InterceptorRepository {

    public static Map<Class<? extends Annotation>, List<CachedAnnotation>> interceptors = Maps.newHashMap();

    public static void add(Annotation annotation, Method method) {
        add(annotation, method, null);
    }

    public static void add(Annotation annotation, Method method, Relationship relationship) {
        if (!interceptors.containsKey(annotation.annotationType())) {
            interceptors.put(annotation.annotationType(), new CopyOnWriteArrayList<CachedAnnotation>());
        }
        List<CachedAnnotation> annotationTypes = interceptors.get(annotation.annotationType());
        annotationTypes.add(new CachedAnnotation(annotation, method, relationship));
    }

    public static List<CachedAnnotation> getInterceptor(Class<? extends Annotation> annotationType) {
        return getInterceptor(annotationType, null);
    }

    public static List<CachedAnnotation> getInterceptor(Class<? extends Annotation> annotationType, CachedAnnotation.InterceptorSelector interceptorSelector) {
        if (interceptors.containsKey(annotationType)) {
            List<CachedAnnotation> interceptorList = interceptors.get(annotationType);
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
        interceptors.clear();
    }

    public static Map<Class<? extends Annotation>, List<CachedAnnotation>> getInterceptorMap() {
        return Collections.unmodifiableMap(interceptors);
    }
}
