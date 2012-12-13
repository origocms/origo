package main.origo.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class InterceptorRepository {

    public static Map<Class<? extends Annotation>, List<CachedAnnotation>> listeners = Maps.newHashMap();

    public static void add(Annotation annotation, Class declaringClass, MethodHandle method) {
        if (!listeners.containsKey(annotation.annotationType())) {
            listeners.put(annotation.annotationType(), new CopyOnWriteArrayList<CachedAnnotation>());
        }
        List<CachedAnnotation> annotationTypes = listeners.get(annotation.annotationType());
        annotationTypes.add(new CachedAnnotation(annotation, declaringClass, method));
    }

    public static List<CachedAnnotation> getInterceptor(Class<? extends Annotation> annotationType) {
        return getInterceptor(annotationType, null);
    }

    public static List<CachedAnnotation> getInterceptor(Class<? extends Annotation> annotationType, CachedAnnotation.ListenerSelector listenerSelector) {
        if (listeners.containsKey(annotationType)) {
            List<CachedAnnotation> listenerList = listeners.get(annotationType);
            if (listenerSelector == null) {
                return listenerList;
            }
            List<CachedAnnotation> matchingListeners = Lists.newArrayList();
            for (CachedAnnotation listener : listenerList) {
                if (listenerSelector.isCorrectListener(listener)) {
                    matchingListeners.add(listener);
                }
            }
            return matchingListeners;
        } else {
            return Collections.emptyList();
        }
    }

    public static void invalidate() {
        listeners.clear();
    }

    public static Map<Class<? extends Annotation>, List<CachedAnnotation>> getInterceptorMap() {
        return Collections.unmodifiableMap(listeners);
    }
}
