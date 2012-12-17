package main.origo.core.helpers.forms;

import main.origo.core.CachedAnnotation;
import main.origo.core.InterceptorRepository;
import main.origo.core.Node;
import main.origo.core.annotations.forms.OnLoadForm;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Helper to trigger \@OnLoad form interceptor. Should not be used directly, use NodeHelper instead.
 *
 * @see main.origo.core.helpers.NodeHelper
 * @see main.origo.core.annotations.OnLoad
 */
public class OnLoadFormHelper {

    public static void triggerBeforeInterceptor(String withType, Node node, String nodeType) {
        triggerBeforeINterceptor(withType, new OnLoadForm.Context(node, nodeType, Collections.<String, Object>emptyMap()));
    }

    public static void triggerBeforeInterceptor(String withType, Node node, String nodeType, Map<String, Object> args) {
        triggerBeforeINterceptor(withType, new OnLoadForm.Context(node, nodeType, args));
    }

    public static void triggerBeforeINterceptor(String withType, OnLoadForm.Context context) {
        List<CachedAnnotation> interceptors = findInterceptorForType(withType, false);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (CachedAnnotation interceptor : interceptors) {
                try {
                    interceptor.method.invoke(null, context);
                } catch (Throwable e) {
                    throw new RuntimeException("", e);
                }
            }
        }
    }

/*
    public static void triggerAfterListenerINterceptor(Node node, Class argType, Object arg, UIElement uiElement) {
        triggerAfterListenerINterceptor(null, node, argType, arg, uiElement);
    }

    public static void triggerAfterListenerINterceptor(String withType, Node node, UIElement uiElement) {
        triggerAfterListenerINterceptor(withType, node, Collections.<Class, Object>singletonMap(UIElement.class, uiElement));
    }
*/

    public static void triggerAfterInterceptor(String withType, Node node, String nodeType) {
        triggerAfterInterceptor(withType, new OnLoadForm.Context(node, nodeType, Collections.<String, Object>emptyMap()));
    }

    public static void triggerAfterInterceptor(String withType, Node node, String nodeType, Map<String, Object> args) {
        triggerAfterInterceptor(withType, new OnLoadForm.Context(node, nodeType, args));
    }

    public static void triggerAfterInterceptor(String withType, OnLoadForm.Context context) {
        List<CachedAnnotation> cachedAnnotationList = findInterceptorForType(!StringUtils.isBlank(withType) ? withType : context.node.getClass().getName(), true);
        if (cachedAnnotationList != null && !cachedAnnotationList.isEmpty()) {
            for (CachedAnnotation cachedAnnotation : cachedAnnotationList) {
                try {
                    cachedAnnotation.method.invoke(null, context);
                } catch (Throwable e) {
                    throw new RuntimeException("", e);
                }
            }
        }
    }

    private static List<CachedAnnotation> findInterceptorForType(final String withType, final boolean after) {
        return InterceptorRepository.getInterceptor(OnLoadForm.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                OnLoadForm annotation = ((OnLoadForm) cachedAnnotation.annotation);
                return annotation.after() == after && (StringUtils.isBlank(annotation.with()) || annotation.with().equals(withType));
            }
        });
    }

}
