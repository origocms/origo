package main.origo.core.event.forms;

import main.origo.core.InterceptorRepository;
import main.origo.core.Node;
import main.origo.core.annotations.forms.OnLoadForm;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.ui.Element;
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
public class OnLoadFormEventGenerator {

    public static void triggerBeforeInterceptor(String nodeType, Node node) {
        triggerBeforeInterceptor(nodeType, node, Collections.<String, Object>emptyMap());
    }

    public static void triggerBeforeInterceptor(String withType, Node node, Map<String, Object> args) {
        triggerBeforeInterceptor(withType, new OnLoadForm.Context(withType, node, args));
    }

    public static void triggerBeforeInterceptor(String withType, OnLoadForm.Context context) {
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
    public static void triggerAfterListenerINterceptor(Node node, Class argType, Object arg, Element element) {
        triggerAfterListenerINterceptor(null, node, argType, arg, element);
    }

    public static void triggerAfterListenerINterceptor(String withType, Node node, Element element) {
        triggerAfterListenerINterceptor(withType, node, Collections.<Class, Object>singletonMap(Element.class, element));
    }
*/

    public static void triggerAfterInterceptor(String withType, Node node) {
        triggerAfterInterceptor(withType, node, Collections.<String, Object>emptyMap());
    }

    public static void triggerAfterInterceptor(String withType, Node node, Element element) {
        triggerAfterInterceptor(withType, node, Collections.<String, Object>emptyMap(), element);
    }

    public static void triggerAfterInterceptor(String withType, Node node, Map<String, Object> args) {
        triggerAfterInterceptor(withType, node, args, null);
    }

    public static void triggerAfterInterceptor(String withType, Node node, Map<String, Object> args, Element element) {
        triggerAfterInterceptor(withType, new OnLoadForm.Context(withType, node, args, element));
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
        return InterceptorRepository.getInterceptors(OnLoadForm.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                OnLoadForm annotation = ((OnLoadForm) cachedAnnotation.annotation);
                return annotation.after() == after && (StringUtils.isBlank(annotation.with()) || annotation.with().equals(withType));
            }
        });
    }

}
