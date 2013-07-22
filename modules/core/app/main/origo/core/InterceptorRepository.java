package main.origo.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import main.origo.core.annotations.Relationship;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.internal.CachedModule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class InterceptorRepository {

    public static Map<Class<? extends Annotation>, Set<CachedAnnotation>> interceptors = Maps.newHashMap();

    public static void add(CachedModule module, Annotation annotation, Method method) {
        add(module, annotation, method, null);
    }

    public static void add(CachedModule module, Annotation annotation, Method method, Relationship relationship) {
        if (!interceptors.containsKey(annotation.annotationType())) {
            interceptors.put(annotation.annotationType(), Sets.<CachedAnnotation>newCopyOnWriteArraySet());
        }
        Set<CachedAnnotation> annotationTypes = interceptors.get(annotation.annotationType());
        annotationTypes.add(new CachedAnnotation(module, annotation, method, relationship));
    }

    public static Set<CachedAnnotation> getInterceptors(Class<? extends Annotation> annotationType) {
        return getInterceptors(annotationType, null);
    }

    public static Set<CachedAnnotation> getInterceptors(Class<? extends Annotation> annotationType, CachedAnnotation.InterceptorSelector interceptorSelector) {
        if (interceptors.containsKey(annotationType)) {
            Set<CachedAnnotation> interceptorList = interceptors.get(annotationType);
            if (interceptorSelector == null) {
                return interceptorList;
            }
            Set<CachedAnnotation> matchingCachedAnnotations = Sets.newHashSet();
            for (CachedAnnotation cachedAnnotation : interceptorList) {
                if (interceptorSelector.isCorrectInterceptor(cachedAnnotation)) {
                    matchingCachedAnnotations.add(cachedAnnotation);
                }
            }
            return matchingCachedAnnotations;
        } else {
            return Collections.emptySet();
        }
    }

    public static void invalidate() {
        interceptors.clear();
    }

    public static Map<Class<? extends Annotation>, Set<CachedAnnotation>> getInterceptorMap() {
        return Collections.unmodifiableMap(interceptors);
    }
}
