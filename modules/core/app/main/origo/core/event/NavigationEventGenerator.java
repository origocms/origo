package main.origo.core.event;

import com.google.common.collect.Maps;
import main.origo.core.Navigation;
import main.origo.core.Node;
import main.origo.core.annotations.Core;
import main.origo.core.ui.NavigationElement;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NavigationEventGenerator {

    /*
     * Convenience methods for hooks with NAVIGATION type
     */
    public static List<NavigationElement> triggerProvidesNavigationInterceptor(Node node, String withType, String section) {
        return ProvidesEventGenerator.triggerInterceptor(node, Core.Type.NAVIGATION, withType, Collections.<String, Object>singletonMap("section", section));
    }

    public static void triggerBeforeNavigationLoaded(Node node, String withType, String section) {
        Map<String, Object> args = Maps.newHashMap();
        args.put("section", section);
        OnLoadEventGenerator.triggerBeforeInterceptor(node, Core.Type.NAVIGATION, withType, args);
    }

    public static void triggerAfterNavigationLoaded(Node node, String withType, Navigation navigation, List<NavigationElement> navigationElements, String section) {
        //TODO: Figure out how to do this with a complete type Collection<NavigationElement>.class? instead of Collection.class
        for (NavigationElement element : navigationElements) {
            OnLoadEventGenerator.triggerAfterInterceptor(node, Core.Type.NAVIGATION, withType, navigation, element, Collections.<String, Object>singletonMap("section", section));
        }
    }

    /*
     * Convenience methods for hooks with NAVIGATION_ITEM type
     */
    public static NavigationElement triggerProvidesNavigationItemInterceptor(Node node, String withType, Navigation navigation) {
        return ProvidesEventGenerator.triggerInterceptor(node, Core.Type.NAVIGATION_ITEM, withType, navigation, Collections.<String, Object>emptyMap());
    }

    public static NavigationElement triggerProvidesNavigationItemInterceptor(Node node, String withType, Navigation navigation, NavigationElement parentNavigationElement) {
        Map<String, Object> args = Maps.newHashMap();
        args.put("parent", parentNavigationElement);
        return ProvidesEventGenerator.triggerInterceptor(node, Core.Type.NAVIGATION_ITEM, withType, navigation, args);
    }

    public static void triggerBeforeNavigationItemLoaded(Node node, String withType, Navigation navigation) {
        OnLoadEventGenerator.triggerBeforeInterceptor(node, Core.Type.NAVIGATION_ITEM, withType, navigation, Collections.<String, Object>emptyMap());
    }

    public static void triggerAfterNavigationItemLoaded(Node node, String withType, Navigation navigation, NavigationElement navigationElement) {
        OnLoadEventGenerator.triggerAfterInterceptor(node, Core.Type.NAVIGATION_ITEM, withType, navigation, navigationElement, Collections.<String, Object>emptyMap());
    }
}
