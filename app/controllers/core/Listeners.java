package controllers.core;

import play.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Listeners {

    public static Map<Class<? extends Annotation>, List<CachedAnnotation>> listeners = new HashMap<Class<? extends Annotation>, List<CachedAnnotation>>();

    public static void addListener(Class<? extends Annotation> annotationClass, String className) {
        List<CachedAnnotation> cachedAnnotations = lookupAnnotatedMethods(annotationClass, className);
        if (cachedAnnotations.isEmpty()) {
            return;
        }
        if (!listeners.containsKey(annotationClass)) {
            listeners.put(annotationClass, new CopyOnWriteArrayList<CachedAnnotation>());
        }
        List<CachedAnnotation> annotationTypes = listeners.get(annotationClass);
        annotationTypes.addAll(cachedAnnotations);
    }

    private static List<CachedAnnotation> lookupAnnotatedMethods(Class<? extends Annotation> annotationClass, String className) {
        Class clz;
        try {
            clz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            Logger.error("Unable to located class \'"+className+"\'");
            return null;
        }

        List<CachedAnnotation> annotatedMethods = new ArrayList<CachedAnnotation>();
        List<Method> methods = findAllAnnotatedMethods(clz);
        for(Method method : methods) {
            Annotation annotation = method.getAnnotation(annotationClass);
            if (annotation != null) {
                annotatedMethods.add(new CachedAnnotation(annotation, method));
            }
        }

        return annotatedMethods;
    }

    public static List<CachedAnnotation> getListenersForAnnotationType(Class<? extends Annotation> annotationType) {
        return getListenersForAnnotationType(annotationType, null);
    }

    public static List<CachedAnnotation> getListenersForAnnotationType(Class<? extends Annotation> annotationType, CachedAnnotation.ListenerSelector listenerSelector) {
        if (listeners.containsKey(annotationType)) {
            List<CachedAnnotation> listenerList = listeners.get(annotationType);
            if (listenerSelector == null) {
                return listenerList;
            }
            List<CachedAnnotation> matchingListeners = new ArrayList<CachedAnnotation>();
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


    private static final Object classAndAnnotationsLock = new Object();
    private static final Map<Class<?>, List<Method>> class2AllMethodsWithAnnotations = new HashMap<Class<?>, List<Method>>();

    /**
     * Find all annotated method from a class
     * @param clazz The class
     * @return A list of method object
     */
    public static List<Method> findAllAnnotatedMethods(Class<?> clazz) {
        synchronized( classAndAnnotationsLock ) {
            // first check the cache..
            List<Method> methods = class2AllMethodsWithAnnotations.get(clazz);
            if( methods != null ) {
                // cache hit
                return methods;
            }
            //have to resolve it..
            methods = new ArrayList<Method>();
            // Clazz can be null if we are looking at an interface / annotation
            while (clazz != null && !clazz.equals(Object.class)) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.getAnnotations().length > 0) {
                        methods.add(method);
                    }
                }
                clazz = clazz.getSuperclass();
            }

            //store it in the cache.
            class2AllMethodsWithAnnotations.put(clazz, methods);
            return methods;
        }
    }

}
