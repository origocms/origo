package main.origo.core.event;

import com.google.common.collect.Maps;
import main.origo.core.Navigation;
import main.origo.core.Node;
import main.origo.core.annotations.Types;
import main.origo.core.ui.NavigationElement;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NavigationEventGenerator {

    /*
     * Convenience methods for hooks with NAVIGATION type
     */
    public static List<NavigationElement> triggerProvidesNavigationInterceptor(String withType, Node node, String section) {
        return ProvidesEventGenerator.triggerInterceptor(Types.NAVIGATION, withType, node, Collections.<String, Object>singletonMap("section", section));
    }

    public static void triggerBeforeNavigationLoaded(String withType, Node node, String section) {
        Map<String, Object> args = Maps.newHashMap();
        args.put("section", section);
        OnLoadEventGenerator.triggerBeforeInterceptor(Types.NAVIGATION, withType, node, args);
    }

    public static void triggerAfterNavigationLoaded(String withType, Node node, List<NavigationElement> navigationElements, String section) {
        //TODO: Figure out how to do this with a complete type Collection<NavigationElement>.class? instead of Collection.class
        OnLoadEventGenerator.triggerAfterInterceptor(Types.NAVIGATION, withType, node, Collections.<String, Object>singletonMap("section", section), navigationElements);
    }

    /*
     * Convenience methods for hooks with NAVIGATION_ITEM type
     */
    public static NavigationElement triggerProvidesNavigationItemInterceptor(String withType, Node node, Navigation navigation) {
        return ProvidesEventGenerator.triggerInterceptor(Types.NAVIGATION_ITEM, withType, node, navigation, Collections.<String, Object>emptyMap());
    }

    public static NavigationElement triggerProvidesNavigationItemInterceptor(String withType, Node node, Navigation navigation, NavigationElement parentNavigationElement) {
        Map<String, Object> args = Maps.newHashMap();
        args.put("parent", parentNavigationElement);
        return ProvidesEventGenerator.triggerInterceptor(Types.NAVIGATION_ITEM, withType, node, navigation, args);
    }

    public static void triggerBeforeNavigationItemLoaded(String withType, Node node, Navigation navigation) {
        OnLoadEventGenerator.triggerBeforeInterceptor(Types.NAVIGATION_ITEM, withType, node, navigation, Collections.<String, Object>emptyMap());
    }

    public static void triggerAfterNavigationItemLoaded(String withType, Node node, Navigation navigation, NavigationElement navigationElement) {
        OnLoadEventGenerator.triggerAfterInterceptor(Types.NAVIGATION_ITEM, withType, node, Collections.<String, Object>emptyMap(), navigation, navigationElement);
    }
}
