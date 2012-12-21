package main.origo.core.helpers;

import main.origo.core.CachedAnnotation;
import main.origo.core.InterceptorRepository;
import main.origo.core.annotations.OnInsertElement;
import main.origo.core.annotations.OnRemoveElement;
import main.origo.core.ui.UIElement;
import org.apache.commons.lang3.StringUtils;
import play.Logger;

import java.util.List;

public class ElementHelper {

    public static void triggerBeforeInsert(UIElement parent, UIElement uiElement) {
        List<CachedAnnotation> interceptors = findOnInsertInterceptors(uiElement.getType(), true);
        for (CachedAnnotation annotation : interceptors) {
            try {
                //noinspection unchecked
                annotation.method.invoke(null, new OnInsertElement.Context(parent, uiElement));
            } catch (Throwable e) {
                Logger.error("", e);
                throw new RuntimeException("Unable to invoke method ["+annotation.method.toString()+"]", e.getCause());
            }
        }
    }

    public static void triggerAfterInsert(UIElement parent, UIElement uiElement) {
        List<CachedAnnotation> interceptors = findOnInsertInterceptors(uiElement.getType(), false);
        for (CachedAnnotation annotation : interceptors) {
            try {
                //noinspection unchecked
                annotation.method.invoke(null, new OnInsertElement.Context(parent, uiElement));
            } catch (Throwable e) {
                Logger.error("", e);
                throw new RuntimeException("Unable to invoke method ["+annotation.method.toString()+"]", e.getCause());
            }
        }
    }

    public static void triggerBeforeRemove(UIElement parent, UIElement uiElement) {
        List<CachedAnnotation> interceptors = findOnInsertInterceptors(uiElement.getType(), true);
        for (CachedAnnotation annotation : interceptors) {
            try {
                //noinspection unchecked
                annotation.method.invoke(null, new OnInsertElement.Context(parent, uiElement));
            } catch (Throwable e) {
                Logger.error("", e);
                throw new RuntimeException("Unable to invoke method ["+annotation.method.toString()+"]", e.getCause());
            }
        }
    }

    public static void triggerAfterRemove(UIElement parent, UIElement uiElement) {
        List<CachedAnnotation> interceptors = findOnRemoveInterceptors(uiElement.getType(), false);
        for (CachedAnnotation annotation : interceptors) {
            try {
                //noinspection unchecked
                annotation.method.invoke(null, new OnInsertElement.Context(parent, uiElement));
            } catch (Throwable e) {
                Logger.error("", e);
                throw new RuntimeException("Unable to invoke method ["+annotation.method.toString()+"]", e.getCause());
            }
        }
    }

    private static List<CachedAnnotation> findOnInsertInterceptors(final String withType, final boolean before) {
        return InterceptorRepository.getInterceptor(OnInsertElement.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                OnInsertElement annotation = (OnInsertElement) cachedAnnotation.annotation;
                return before != annotation.after() && (annotation.with().equals(withType) || StringUtils.isBlank(withType));
            }
        });
    }

    private static List<CachedAnnotation> findOnRemoveInterceptors(final String withType, final boolean before) {
        return InterceptorRepository.getInterceptor(OnRemoveElement.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                OnRemoveElement annotation = (OnRemoveElement) cachedAnnotation.annotation;
                return before != annotation.after() && (annotation.with().equals(withType) || StringUtils.isBlank(withType));
            }
        });
    }
}
