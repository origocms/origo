package main.origo.core.helpers;

import com.google.common.collect.Maps;
import main.origo.core.CachedAnnotation;
import main.origo.core.InterceptorRepository;
import main.origo.core.annotations.OnInsertElement;
import main.origo.core.annotations.OnRemoveElement;
import main.origo.core.ui.Element;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.api.templates.Html;

import java.util.List;
import java.util.Map;

public class ElementHelper {

    public static void triggerBeforeInsert(Element parent, Element element) {
        List<CachedAnnotation> interceptors = findOnInsertInterceptors(element.getType(), true);
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
        List<CachedAnnotation> interceptors = findOnInsertInterceptors(element.getType(), false);
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
        List<CachedAnnotation> interceptors = findOnInsertInterceptors(element.getType(), true);
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
        List<CachedAnnotation> interceptors = findOnRemoveInterceptors(element.getType(), false);
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

    public static Html getHtmlFromBody(Element element) {
        if (element.hasBody()) {
            return element.getBody();
        } else {
            return Html.empty();
        }
    }

    public static Map<String, String> combineAttributes(Map<String, String> map1, Map<String, String> map2) {
        Map<String, String> result = Maps.newHashMap(map1);
        for (String name : map2.keySet()) {
            if (result.containsKey(name)) {
                result.put(name, result.get(name).concat(" ").concat(map2.get(name)));
            } else {
                result.put(name, map2.get(name));
            }
        }
        return result;
    }

}
