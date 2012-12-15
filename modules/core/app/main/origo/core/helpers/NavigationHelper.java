package main.origo.core.helpers;

import com.google.common.collect.Maps;
import main.origo.core.Navigation;
import main.origo.core.Node;
import main.origo.core.annotations.Types;
import main.origo.core.ui.NavigationElement;
import models.origo.core.RootNode;

import java.util.*;

public class NavigationHelper {

    public static Collection<NavigationElement> getNavigation(Node node, String section) {
        String navigationType = SettingsHelper.Core.getNavigationType();
        return triggerProvidesNavigationListener(navigationType, node, section);
    }

    /*
     * Convenience methods for hooks with NAVIGATION type
     */
    public static Collection<NavigationElement> triggerProvidesNavigationListener(String withType, Node node, String section) {
        return ProvidesHelper.triggerListener(Types.NAVIGATION, withType, node, Collections.<String, Object>singletonMap("section", section));
    }

    public static void triggerBeforeNavigationLoaded(String withType, Node node, String section) {
        Map<String, Object> args = Maps.newHashMap();
        args.put("section", section);
        OnLoadHelper.triggerBeforeListener(Types.NAVIGATION, withType, node, args);
    }

    public static void triggerAfterNavigationLoaded(String withType, Node node, List<NavigationElement> navigationElements, String section) {
        //TODO: Figure out how to do this with a complete type Collection<NavigationElement>.class? instead of Collection.class
        OnLoadHelper.triggerAfterListener(Types.NAVIGATION, withType, node, Collections.<String, Object>singletonMap("section", section), navigationElements);
    }

    /*
     * Convenience methods for hooks with NAVIGATION_ITEM type
     */
    public static NavigationElement triggerProvidesNavigationItemListener(String withType, Node node, Navigation navigation) {
        return ProvidesHelper.triggerListener(Types.NAVIGATION_ITEM, withType, node, navigation, Collections.<String, Object>emptyMap());
    }

    public static NavigationElement triggerProvidesNavigationItemListener(String withType, Node node, Navigation navigation, NavigationElement navigationElement) {
        Map<String, Object> args = Maps.newHashMap();
        args.put("navigation", navigation);
        args.put("navigation_element", navigationElement);
        return ProvidesHelper.triggerListener(Types.NAVIGATION_ITEM, withType, node, args);
    }

    public static void triggerBeforeNavigationItemLoaded(String withType, Node node, Navigation navigation) {
        OnLoadHelper.triggerBeforeListener(Types.NAVIGATION_ITEM, withType, node, navigation, Collections.<String, Object>emptyMap());
    }

    public static void triggerAfterNavigationItemLoaded(String withType, Node node, Navigation navigation, NavigationElement navigationElement) {
        OnLoadHelper.triggerAfterListener(Types.NAVIGATION_ITEM, withType, node, Collections.<String, Object>emptyMap(), navigation, navigationElement);
    }

}
