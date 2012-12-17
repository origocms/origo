package main.origo.core.helpers;

import com.google.common.collect.Maps;
import main.origo.core.Navigation;
import main.origo.core.Node;
import main.origo.core.annotations.Types;
import main.origo.core.ui.NavigationElement;

import java.util.*;

public class NavigationHelper {

    public static List<NavigationElement> getNavigation(Node node, String section) {
        String navigationType = SettingsHelper.Core.getNavigationType();
        return triggerProvidesNavigationInterceptor(navigationType, node, section);
    }

    /*
     * Convenience methods for hooks with NAVIGATION type
     */
    public static List<NavigationElement> triggerProvidesNavigationInterceptor(String withType, Node node, String section) {
        return ProvidesHelper.triggerInterceptor(Types.NAVIGATION, withType, node, Collections.<String, Object>singletonMap("section", section));
    }

    public static void triggerBeforeNavigationLoaded(String withType, Node node, String section) {
        Map<String, Object> args = Maps.newHashMap();
        args.put("section", section);
        OnLoadHelper.triggerBeforeInterceptor(Types.NAVIGATION, withType, node, args);
    }

    public static void triggerAfterNavigationLoaded(String withType, Node node, List<NavigationElement> navigationElements, String section) {
        //TODO: Figure out how to do this with a complete type Collection<NavigationElement>.class? instead of Collection.class
        OnLoadHelper.triggerAfterInterceptor(Types.NAVIGATION, withType, node, Collections.<String, Object>singletonMap("section", section), navigationElements);
    }

    /*
     * Convenience methods for hooks with NAVIGATION_ITEM type
     */
    public static NavigationElement triggerProvidesNavigationItemInterceptor(String withType, Node node, Navigation navigation) {
        return ProvidesHelper.triggerInterceptor(Types.NAVIGATION_ITEM, withType, node, navigation, Collections.<String, Object>emptyMap());
    }

    public static NavigationElement triggerProvidesNavigationItemInterceptor(String withType, Node node, Navigation navigation, NavigationElement parentNavigationElement) {
        Map<String, Object> args = Maps.newHashMap();
        args.put("parent", parentNavigationElement);
        return ProvidesHelper.triggerInterceptor(Types.NAVIGATION_ITEM, withType, node, navigation, args);
    }

    public static void triggerBeforeNavigationItemLoaded(String withType, Node node, Navigation navigation) {
        OnLoadHelper.triggerBeforeInterceptor(Types.NAVIGATION_ITEM, withType, node, navigation, Collections.<String, Object>emptyMap());
    }

    public static void triggerAfterNavigationItemLoaded(String withType, Node node, Navigation navigation, NavigationElement navigationElement) {
        OnLoadHelper.triggerAfterInterceptor(Types.NAVIGATION_ITEM, withType, node, Collections.<String, Object>emptyMap(), navigation, navigationElement);
    }

}
