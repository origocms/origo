package main.origo.core.helpers;

import main.origo.core.CachedAnnotation;
import main.origo.core.InterceptorRepository;
import main.origo.core.Navigation;
import main.origo.core.Node;
import main.origo.core.annotations.OnLoad;
import main.origo.core.ui.NavigationElement;
import main.origo.core.ui.UIElement;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Helper to trigger \@OnLoad listeners. Should not be used directly, use NodeHelper instead.
 *
 * @see NodeHelper
 * @see main.origo.core.annotations.OnLoad
 */
public class OnLoadHelper {

    public static void triggerBeforeListener(String type, String withType, Node node, Map<String, Object> args) {
        List<CachedAnnotation> listeners = findListenerForType(type, withType, false);
        if (listeners != null && !listeners.isEmpty()) {
            for (CachedAnnotation listener : listeners) {
                try {
                    listener.method.invokeExact(listener.declaringClass, new OnLoad.Context(node, args));
                } catch (Throwable e) {
                    throw new RuntimeException("", e);
                }
            }
        }
    }

    public static void triggerBeforeListener(String type, String withType, Node node, Navigation navigation, Map<String, Object> args) {
        List<CachedAnnotation> listeners = findListenerForType(type, withType, false);
        if (listeners != null && !listeners.isEmpty()) {
            for (CachedAnnotation listener : listeners) {
                try {
                    listener.method.invokeExact(listener.declaringClass, new OnLoad.Context(node, navigation, args));
                } catch (Throwable e) {
                    throw new RuntimeException("", e);
                }
            }
        }
    }

    public static void triggerAfterListener(String onLoadType, String withType, Node node, Map<String, Object> args) {
        List<CachedAnnotation> listeners = findListenerForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (listeners != null && !listeners.isEmpty()) {
            for (CachedAnnotation listener : listeners) {
                try {
                    listener.method.invokeExact(listener.declaringClass, new OnLoad.Context(node, args));
                } catch (Throwable e) {
                    throw new RuntimeException("", e);
                }
            }
        }
    }

    public static void triggerAfterListener(String onLoadType, String withType, Node node, Map<String, Object> args, UIElement uiElement) {
        List<CachedAnnotation> listeners = findListenerForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (listeners != null && !listeners.isEmpty()) {
            for (CachedAnnotation listener : listeners) {
                try {
                    listener.method.invokeExact(listener.declaringClass, new OnLoad.Context(node, uiElement, args));
                } catch (Throwable e) {
                    throw new RuntimeException("", e);
                }
            }
        }
    }

    public static void triggerAfterListener(String onLoadType, String withType, Node node, Map<String, Object> args, Navigation navigation) {
        List<CachedAnnotation> listeners = findListenerForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (listeners != null && !listeners.isEmpty()) {
            for (CachedAnnotation listener : listeners) {
                try {
                    listener.method.invokeExact(listener.declaringClass, new OnLoad.Context(node, navigation, args));
                } catch (Throwable e) {
                    throw new RuntimeException("", e);
                }
            }
        }
    }

    public static void triggerAfterListener(String onLoadType, String withType, Node node, Map<String, Object> args, Navigation navigation, NavigationElement navigationElement) {
        List<CachedAnnotation> listeners = findListenerForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (listeners != null && !listeners.isEmpty()) {
            for (CachedAnnotation listener : listeners) {
                try {
                    listener.method.invokeExact(listener.declaringClass, new OnLoad.Context(node, navigation, navigationElement, args));
                } catch (Throwable e) {
                    throw new RuntimeException("", e);
                }
            }
        }
    }

    public static void triggerAfterListener(String onLoadType, String withType, Node node, Map<String, Object> args, List<NavigationElement> navigationElements) {
        List<CachedAnnotation> listeners = findListenerForType(onLoadType, !StringUtils.isBlank(withType) ? withType : node.getClass().getName(), true);
        if (listeners != null && !listeners.isEmpty()) {
            for (CachedAnnotation listener : listeners) {
                try {
                    listener.method.invokeExact(listener.declaringClass, new OnLoad.Context(node, navigationElements, args));
                } catch (Throwable e) {
                    throw new RuntimeException("", e);
                }
            }
        }
    }

    private static List<CachedAnnotation> findListenerForType(final String onLoadType, final String withType, final boolean after) {
        return InterceptorRepository.getInterceptor(OnLoad.class, new CachedAnnotation.ListenerSelector() {
            @Override
            public boolean isCorrectListener(CachedAnnotation listener) {
                OnLoad annotation = ((OnLoad) listener.annotation);
                return annotation.type().equals(onLoadType) && annotation.after() == after &&
                        (StringUtils.isBlank(annotation.with()) || annotation.with().equals(withType));
            }
        });
    }
}
