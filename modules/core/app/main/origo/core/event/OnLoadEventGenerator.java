package main.origo.core.event;

import main.origo.core.InterceptorRepository;
import main.origo.core.Navigation;
import main.origo.core.Node;
import main.origo.core.annotations.OnLoad;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.ui.Element;
import main.origo.core.ui.NavigationElement;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Helper to trigger \@OnLoad interceptors. Should not be used directly, use NodeHelper instead.
 *
 * @see main.origo.core.helpers.NodeHelper
 * @see main.origo.core.annotations.OnLoad
 */
public class OnLoadEventGenerator {

    private static void triggerBeforeInterceptor(String type, String withType, OnLoad.Context context) {
        List<CachedAnnotation> interceptors = findInterceptorForType(type, !StringUtils.isBlank(withType) ? withType : context.node.getClass().getName(), false);
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

    public static void triggerBeforeInterceptor(Node node, String type, String withType, Map<String, Object> args) {
        triggerBeforeInterceptor(type, withType, new OnLoad.Context(node, args));
    }

    public static void triggerBeforeInterceptor(Node node, String type, String withType, Navigation navigation, Map<String, Object> args) {
        triggerBeforeInterceptor(type, withType, new OnLoad.Context(node, navigation, args));
    }

    public static void triggerBeforeInterceptor(Node node, String type, String withType) {
        triggerBeforeInterceptor(node, type, withType, Collections.<String, Object>emptyMap());
    }

    private static void triggerAfterInterceptor(String onLoadType, String withType, OnLoad.Context context) {
        List<CachedAnnotation> interceptorList = findInterceptorForType(onLoadType, !StringUtils.isBlank(withType) ? withType : context.node.getClass().getName(), true);
        if (interceptorList != null && !interceptorList.isEmpty()) {
            for (CachedAnnotation interceptor : interceptorList) {
                try {
                    interceptor.method.invoke(null, context);
                } catch (Throwable e) {
                    throw new RuntimeException("", e);
                }
            }
        }
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType, Map<String, Object> args) {
        triggerAfterInterceptor(onLoadType, withType, new OnLoad.Context(node, args));
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType, Map<String, Object> args, Element element) {
        triggerAfterInterceptor(onLoadType, withType, new OnLoad.Context(node, element, args));
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType, Map<String, Object> args, Navigation navigation) {
        triggerAfterInterceptor(onLoadType, withType, new OnLoad.Context(node, navigation, args));
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType, Map<String, Object> args, Navigation navigation, NavigationElement navigationElement) {
        triggerAfterInterceptor(onLoadType, withType, new OnLoad.Context(node, navigation, navigationElement, args));
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType, Map<String, Object> args, List<NavigationElement> navigationElements) {
        triggerAfterInterceptor(onLoadType, withType, new OnLoad.Context(node, navigationElements, args));
    }

    public static void triggerAfterInterceptor(Node node, String onLoadType, String withType) {
        triggerAfterInterceptor(node, onLoadType, withType, Collections.<String, Object>emptyMap());
    }

    private static List<CachedAnnotation> findInterceptorForType(final String onLoadType, final String withType, final boolean after) {
        return InterceptorRepository.getInterceptors(OnLoad.class, new CachedAnnotation.InterceptorSelector() {
            @Override
            public boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation) {
                OnLoad annotation = ((OnLoad) cachedAnnotation.annotation);
                return annotation.type().equals(onLoadType) && annotation.after() == after &&
                        (StringUtils.isBlank(annotation.with()) || annotation.with().equals(withType));
            }
        });
    }

}
