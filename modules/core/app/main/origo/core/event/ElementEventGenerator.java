package main.origo.core.event;

import main.origo.core.InterceptorRepository;
import main.origo.core.annotations.OnInsertElement;
import main.origo.core.annotations.OnRemoveElement;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.ui.Element;
import play.Logger;

import java.util.List;

public class ElementEventGenerator {

    public static void triggerBeforeInsert(Element parent, Element element) {
        List<CachedAnnotation> interceptors = findOnInsertInterceptors(element.getClass(), element.getInputType(), true);
        for (CachedAnnotation annotation : interceptors) {
            try {
                //noinspection unchecked
                annotation.method.invoke(null, new OnInsertElement.Context(parent, element));
            } catch (Throwable e) {
                Logger.error("", e);
                throw new RuntimeException("Unable to invoke method [" + annotation.method.toString() + "]", e.getCause());
            }
        }
    }

    public static void triggerAfterInsert(Element parent, Element element) {
        List<CachedAnnotation> interceptors = findOnInsertInterceptors(element.getClass(), element.getInputType(), false);
        for (CachedAnnotation annotation : interceptors) {
            try {
                //noinspection unchecked
                annotation.method.invoke(null, new OnInsertElement.Context(parent, element));
            } catch (Throwable e) {
                Logger.error("", e);
                throw new RuntimeException("Unable to invoke method [" + annotation.method.toString() + "]", e.getCause());
            }
        }
    }

    public static void triggerBeforeRemove(Element parent, Element element) {
        List<CachedAnnotation> interceptors = findOnRemoveInterceptors(element.getClass(), element.getInputType(), true);
        for (CachedAnnotation annotation : interceptors) {
            try {
                //noinspection unchecked
                annotation.method.invoke(null, new OnInsertElement.Context(parent, element));
            } catch (Throwable e) {
                Logger.error("", e);
                throw new RuntimeException("Unable to invoke method [" + annotation.method.toString() + "]", e.getCause());
            }
        }
    }

    public static void triggerAfterRemove(Element parent, Element element) {
        List<CachedAnnotation> interceptors = findOnRemoveInterceptors(element.getClass(), element.getInputType(), false);
        for (CachedAnnotation annotation : interceptors) {
            try {
                //noinspection unchecked
                annotation.method.invoke(null, new OnInsertElement.Context(parent, element));
            } catch (Throwable e) {
                Logger.error("", e);
                throw new RuntimeException("Unable to invoke method [" + annotation.method.toString() + "]", e.getCause());
            }
        }
    }

    private static List<CachedAnnotation> findOnInsertInterceptors(final Class withType, final Class inputType, final boolean before) {
        return InterceptorRepository.getInterceptors(OnInsertElement.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                OnInsertElement annotation = (OnInsertElement) cachedAnnotation.annotation;
                return before != annotation.after() && (annotation.with().equals(withType) || withType == null) &&
                        (inputType == null || annotation.input().equals(inputType));
            }
        });
    }

    private static List<CachedAnnotation> findOnRemoveInterceptors(final Class withType, final Class inputType, final boolean before) {
        return InterceptorRepository.getInterceptors(OnRemoveElement.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                OnRemoveElement annotation = (OnRemoveElement) cachedAnnotation.annotation;
                return before != annotation.after() && (annotation.with().equals(withType) || withType == null) &&
                        (inputType == null || annotation.input().equals(inputType));
            }
        });
    }
}
